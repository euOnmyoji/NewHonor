package com.github.euonmyoji.newhonor.command.args;

import com.github.euonmyoji.newhonor.NewHonor;
import com.github.euonmyoji.newhonor.configuration.HonorConfig;
import com.github.euonmyoji.newhonor.manager.LanguageManager;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yinyangshi
 */
@NonnullByDefault
public class HonorIDArg extends CommandElement {
    private final boolean shouldPresent;
    private final Level level;

    public HonorIDArg(@Nullable Text key) {
        this(key, true, Level.ERROR);
    }

    public HonorIDArg(@Nullable Text key, boolean shouldPresent, Level level) {
        super(key);
        this.shouldPresent = shouldPresent;
        this.level = level;
    }

    @Nullable
    @Override
    protected Object parseValue(CommandSource src, CommandArgs args) throws ArgumentParseException {
        String arg = args.next();
        if (shouldPresent) {
            if (level == Level.ERROR && !HonorConfig.getAllCreatedHonors().contains(arg)) {
                throw args.createError(LanguageManager.langBuilder("newhonor.command.arg.error.honornotpresent",
                        "The honorid is not present")
                        .replaceHonorid(arg).build());
            }
            if (level == Level.WARNING && !HonorConfig.getAllCreatedHonors().contains(arg)) {
                src.sendMessage(Text.of(LanguageManager.langBuilder("newhonor.command.arg.warn.honornotpresent",
                        "[Warn]The honorid should present but it's not present.")
                        .replaceHonorid(arg).build()));
            }
        }
        return arg;
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        if (shouldPresent) {
            if (args.hasNext()) {
                try {
                    String arg = args.next();
                    return HonorConfig.getAllCreatedHonors().stream().filter(s -> s.startsWith(arg)).collect(Collectors.toList());
                } catch (ArgumentParseException e) {
                    NewHonor.logger.debug("unknown error", e);
                }
            }
            return new ArrayList<>(HonorConfig.getAllCreatedHonors());
        }

        return Collections.emptyList();
    }


    @Override
    public Text getUsage(CommandSource src) {
        return Text.of("<honorid>");
    }

}
