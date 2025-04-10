package net.byebye.balance;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Events implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        OfflinePlayer target = Bukkit.getOfflinePlayer(Commands.pagamentoConfirmacao.get(player.getUniqueId()));

        Player targetonline = target.getPlayer();

        Double valor = Commands.pagamentoValor.get(player.getUniqueId());
        Economy economy = new Economy();

        Double saldoplayer = economy.getSaldo(player);
        //Double saldotarget = economy.getSaldo(target);

        Location loc = player.getLocation();

        //Tela do /pagar
        if (e.getView().getTitle().equalsIgnoreCase("§eConfirmação de Pagamento")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType() == Material.LIME_WOOL) {
                player.closeInventory();
                if (saldoplayer < valor) {
                    player.sendMessage("§cVocê não dinheiro suficiente.");
                    player.playSound(loc, Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return;
                }
                economy.addSaldo(target, valor);
                economy.removeSaldo(player, valor);
                player.playSound(loc, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                player.sendMessage("§eVocê pagou §aR$" + valor + " §ea §d" + target.getName());
                targetonline.sendMessage("§eVocê recebeu §aR$" + valor + " §ede §d" + player.getName());
            } else if (e.getCurrentItem().getType() == Material.RED_WOOL) {
                player.sendMessage("§cVocê cancelou o pagamento.");
                player.closeInventory();
            }
        }
    }
}
