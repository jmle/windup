package org.jboss.windup.reporting.data.rules;

import org.apache.commons.io.FileUtils;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.addons.Addon;
import org.jboss.forge.furnace.util.AddonFilters;
import org.jboss.windup.config.AbstractRuleProvider;
import org.jboss.windup.config.GraphRewrite;
import org.jboss.windup.config.loader.RuleLoaderContext;
import org.jboss.windup.config.metadata.RuleMetadata;
import org.jboss.windup.config.operation.GraphOperation;
import org.jboss.windup.config.phase.PostReportPfRenderingPhase;
import org.jboss.windup.graph.GraphContext;
import org.jboss.windup.reporting.service.ReportService;
import org.jboss.windup.util.ZipUtil;
import org.ocpsoft.logging.Logger;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.context.EvaluationContext;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RuleMetadata(
        phase = PostReportPfRenderingPhase.class,
        haltOnException = true
)
public class UIRuleProvider extends AbstractRuleProvider {

    private static final Logger LOG = Logger.getLogger(UIRuleProvider.class);

    private final static String UI_ZIP_FILENAME = "pf-windup-ui.zip";
    private final static String THEME_JS_FILENAME = "theme.js";

    @Inject
    private Furnace furnace;

    // @formatter:off
    @Override
    public Configuration getConfiguration(RuleLoaderContext ruleLoaderContext) {
        return ConfigurationBuilder.begin()
                .addRule()
                .perform(new GraphOperation() {
                    @Override
                    public void perform(GraphRewrite event, EvaluationContext context) {
                        performProcess(event);
                    }
                });
    }
    // @formatter:on

    private void performProcess(GraphRewrite event) {
        GraphContext context = event.getGraphContext();
        ReportService reportService = new ReportService(context);

        Path uiDirectory = reportService.getWindupUIDirectory();
        Path uiApiDirectory = reportService.getWindupUIApiDirectory();

        try {
            FileUtils.forceMkdir(uiDirectory.toFile());

            // Copy UI ZIP
            InputStream uiZipInputStream = null;
            for (Addon addon : furnace.getAddonRegistry().getAddons(AddonFilters.allLoaded())) {
                uiZipInputStream = addon.getClassLoader().getResourceAsStream(UI_ZIP_FILENAME);
                if (uiZipInputStream != null) {
                    break;
                }
            }
            if (uiZipInputStream == null) {
                throw new IllegalStateException("Could not find UI");
            }

            Files.copy(uiZipInputStream, uiDirectory.resolve(UI_ZIP_FILENAME));

            // Unzip UI
            File uiZipFile = uiDirectory.resolve(UI_ZIP_FILENAME).toFile();
            ZipUtil.unzipToFolder(uiZipFile, uiDirectory.toFile());

            // Copy windup.js
            Path sourceWindupJS = uiApiDirectory.resolve(AbstractApiRuleProvider.JAVASCRIPT_OUTPUT);

            Path targetWindupJS = uiDirectory.resolve(AbstractApiRuleProvider.JAVASCRIPT_OUTPUT); // "The 'api' folder represents 'pf-ui/src/main/webapp/public'"
            Files.delete(targetWindupJS);

            Files.copy(sourceWindupJS, targetWindupJS);
            Files.delete(sourceWindupJS);

            // Clean
            Files.delete(uiDirectory.resolve(UI_ZIP_FILENAME));

            // Add theming config
            try (InputStream themeJSInputStream = UIRuleProvider.class.getClassLoader().getResourceAsStream(THEME_JS_FILENAME)) {
                if (themeJSInputStream == null) {
                    try (InputStream anotherThemeJSInputStream = furnace.getRuntimeClassLoader().getResourceAsStream(THEME_JS_FILENAME)) {
                        if (anotherThemeJSInputStream != null) {
                            setupTheme(uiDirectory, anotherThemeJSInputStream);
                        } else {
                            throw new IllegalStateException("Could not find theme.js config file");
                        }
                    }
                } else {
                    setupTheme(uiDirectory, themeJSInputStream);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupTheme(Path uiDirectory, InputStream themeJSInputStream) throws IOException {
        Files.delete(uiDirectory.resolve(THEME_JS_FILENAME));
        Files.copy(themeJSInputStream, uiDirectory.resolve(THEME_JS_FILENAME));
    }
}
