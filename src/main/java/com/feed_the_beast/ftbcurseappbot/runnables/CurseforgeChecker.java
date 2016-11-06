package com.feed_the_beast.ftbcurseappbot.runnables;

import com.beust.jcommander.internal.Lists;
import com.feed_the_beast.ftbcurseappbot.Config;
import com.feed_the_beast.ftbcurseappbot.Main;
import com.feed_the_beast.javacurselib.addondumps.Addon;
import com.feed_the_beast.javacurselib.addondumps.Bz2Data;
import com.feed_the_beast.javacurselib.addondumps.DatabaseType;
import com.feed_the_beast.javacurselib.addondumps.MergedDatabase;
import com.feed_the_beast.javacurselib.addondumps.ReleaseType;
import com.feed_the_beast.javacurselib.service.contacts.contacts.ContactsResponse;
import com.feed_the_beast.javacurselib.utils.CurseGUID;
import com.feed_the_beast.javacurselib.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

@Slf4j
public class CurseforgeChecker implements Runnable {
    private WebSocket webSocket;
    private boolean initialized = false;
    private Optional<List<String>> channelsEnabled;//TODO make sure this gets updates!!

    public CurseforgeChecker (@Nonnull WebSocket webSocket) {
        this.webSocket = webSocket;
        this.channelsEnabled = Optional.of(Lists.newArrayList());
        channelsEnabled.get().add("Progwml6's mods.curseforge-updates");
    }

    @Override
    public void run () {
        boolean changed = false;
        Thread.currentThread().setName("curseforgecheckthread");
        String result = "";
        if (!initialized) {
            initialized = true;
            Main.getCacheService().setAddonDatabase(Bz2Data.getDatabase(Bz2Data.MC_GAME_ID, DatabaseType.COMPLETE));
            log.info("Curseforge Checker Initialized");
            return;
        } else {
            MergedDatabase db = Bz2Data.updateCompleteDatabaseIfNeeded(Main.getCacheService().getAddonDatabase(), Bz2Data.MC_GAME_ID);
            if (db.changes != null) {
                changed = true;
                result = "Curse Updates: ";
                String mods = "";
                String packs = "";
                String tps = "";
                for (Addon a : db.changes.data) {
                    if (a.categorySection.path.equals("mods")) {
                        if (mods.isEmpty()) {
                            mods += "Mods: ";
                        }
                        mods += a.name + getFeed(a.latestFiles.get(0).releaseType) + " for minecraft: ";
                        for (String s : a.latestFiles.get(0).gameVersion) {
                            if (!mods.endsWith(", ") || mods.endsWith(": ")) {
                                mods += ", ";
                            }
                            mods += s;
                        }
                    } else if (a.categorySection.path.equals("resourcepacks")) {
                        if (tps.isEmpty()) {
                            tps += "Resource Packs: ";
                        }
                        tps += a.name + getFeed(a.latestFiles.get(0).releaseType) + " for minecraft: ";
                        for (String s : a.latestFiles.get(0).gameVersion) {
                            if (!tps.endsWith(", ") || tps.endsWith(": ")) {
                                tps += ", ";
                            }
                            tps += s;
                        }

                    } else if (a.categorySection.name.equals("Modpacks")) {
                        if (packs.isEmpty()) {
                            packs += "ModPacks: ";
                        }
                        packs += a.name + getFeed(a.latestFiles.get(0).releaseType) + " for minecraft: ";
                        for (String s : a.latestFiles.get(0).gameVersion) {
                            if (!packs.endsWith(", ") || packs.endsWith(": ")) {
                                packs += ", ";
                            }
                            packs += s;
                        }

                    }
                }
                result += mods;
                if (!result.endsWith(": ")) {
                    result += ", ";
                }
                result += packs;
                if (!result.endsWith(": ") || !result.endsWith(", ")) {
                    result += ", ";
                }
                result += tps;
                Main.getCacheService().setAddonDatabase(db.currentDatabase);
            }
        }
        if (changed) {
            log.debug("curseforge changes detected");
            sendServiceStatusNotifications(Main.getCacheService().getContacts().get(), webSocket, result, this.channelsEnabled);
        } else {
            log.debug("No curseforge change detected + db_timestamp: " + Main.getCacheService().getAddonDatabase().timestamp + " Now: " + new Date().getTime());
        }
    }

    public static String getFeed (ReleaseType r) {
        switch (r) {
        case ALPHA:
            return " alpha";
        case BETA:
            return " beta";
        case RELEASE:
            return " release";
        default:
            return " UNKNOWN " + r.getValue();
        }
    }

    public void sendServiceStatusNotifications (@Nonnull ContactsResponse cr, @Nonnull WebSocket ws, @Nonnull String message, @Nonnull java.util.Optional<List<String>> channelsEnabled) {
        if (message.isEmpty()) {
            if (Config.isDebugEnabled()) {
                log.debug("no CurseForge Updates");
            }
            return;
        }
        if (channelsEnabled.isPresent()) {
            log.info("{} has had an update change");
            for (String s : channelsEnabled.get()) {
                log.info("sending {} change to {}", "CurseForge", s);
                if (s.contains(".")) {
                    String[] g = s.split("\\.");
                    Optional<CurseGUID> ci = Main.getCacheService().getContacts().get().getChannelIdbyNames(g[0], g[1], true);
                    if (ci.isPresent()) {
                        log.debug("sending status change for {} to {} guid: {}", "CurseForge", s, ci.get().serialize());
                        ws.sendMessage(ci.get(), message);
                    } else {
                        log.error("no channel id exists for {} {}", g[0], g[1]);
                    }
                } else {
                    Optional<CurseGUID> ci = Main.getCacheService().getContacts().get().getGroupIdByName(s, String::equalsIgnoreCase);
                    if (ci.isPresent()) {
                        ws.sendMessage(ci.get(), message);
                    } else {
                        log.error("no channel id exists for {}", s);
                    }

                }
            }
        }
    }

}