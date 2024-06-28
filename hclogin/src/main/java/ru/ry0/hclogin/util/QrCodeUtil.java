package ru.ry0.hclogin.util;

import com.google.zxing.WriterException;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.io.IOException;

public class QrCodeUtil {
    public static void sendQrCodeToPlayer(Player player, String url) throws WriterException, IOException {
        Component msgc = Component.text("Нажми ")
                .color(TextColor.color(0x13f832))
                .append(Component.text("сюда", NamedTextColor.AQUA).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, url)).decorate(TextDecoration.UNDERLINED))
                .append(Component.text(" чтобы войти (" + url + ")"))
                .color(TextColor.color(0x13f832));
        player.sendMessage(msgc);
        player.spoofChatInput("/cinv");
        player.spoofChatInput("/qr create " + url);
    }
}
