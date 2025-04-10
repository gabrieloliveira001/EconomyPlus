package net.byebye.balance;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Commands implements CommandExecutor {

    public static final Map<UUID, UUID> pagamentoConfirmacao = new HashMap<>();
    public static final Map<UUID, Double> pagamentoValor = new HashMap<>();

    public void PagarGui(Player player, OfflinePlayer target, Double valor) {

        Inventory gui = Bukkit.createInventory(null, 27, "§eConfirmação de Pagamento");

        ItemStack item1 = new ItemStack(Material.RED_WOOL);
        ItemMeta meta1 = item1.getItemMeta();
        meta1.setDisplayName("§cNão");
        meta1.setLore(Collections.singletonList(
                "§7Clique para §ccancelar §7o pagamento."
        ));
        item1.setItemMeta(meta1);


        ItemStack item2 = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta2 = (SkullMeta) item2.getItemMeta();
        meta2.setOwningPlayer(target);
        meta2.setDisplayName("§e" + target.getName());
        meta2.setLore(Collections.singletonList(
                "§7Você deseja pagar §aR$" + valor + " §7para esse jogador?"
        ));
        item2.setItemMeta(meta2);


        ItemStack item3 = new ItemStack(Material.LIME_WOOL);
        ItemMeta meta3 = item3.getItemMeta();
        meta3.setDisplayName("§aSim");
        meta3.setLore(Collections.singletonList(
                "§7Clique para §aconfirmar §7o pagamento."
        ));
        item3.setItemMeta(meta3);


        gui.setItem(10, item1);
        gui.setItem(13, item2);
        gui.setItem(16, item3);

        pagamentoConfirmacao.put(player.getUniqueId(), target.getUniqueId());
        pagamentoValor.put(player.getUniqueId(), valor);
        player.openInventory(gui);


    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        Economy economy = new Economy();

        if (command.getName().equalsIgnoreCase("saldo")) {
            double saldo = economy.getSaldo(player);
            player.sendMessage("§eSaldo: §aR$" + saldo);
        }


        if (command.getName().equalsIgnoreCase("versaldo")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if (args.length < 1) {
                player.sendMessage("§cUse /versaldo <player>");
                return true;
            }

            player.sendMessage("§eSaldo de §d" + target.getName() + ": §aR$" + economy.getSaldo(target));
        }

        if (command.getName().equalsIgnoreCase("pagar")) {
            if (args.length < 2) {
                player.sendMessage("§cUse /pagar <player> <valor>.");
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            double valor = Double.parseDouble(args[1]);

            if (target == player) {
                player.sendMessage("§cVocê não pode mandar pagamentos para si mesmo");
                return true;
            }
            PagarGui(player, target, valor);
        }

        if (command.getName().equalsIgnoreCase("addsaldo")) {
            if (args.length < 2) {
                player.sendMessage("§cUse /addsaldo <player> <valor>.");
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            double valor = Double.parseDouble(args[1]);

            economy.addSaldo(target, valor);
            player.sendMessage("§eO valor §aR$" + valor + " §efoi adicionado a conta de §a" + target.getName() + ".");
        }

        if (command.getName().equalsIgnoreCase("setsaldo")) {
            if (args.length < 2) {
                player.sendMessage("§cUse /setsaldo <player> <valor>.");
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            double valor = Double.parseDouble(args[1]);

            economy.setSaldo(target, valor);
            player.sendMessage("§eO saldo de §a" + target.getName() + " §efoi setado para §aR$" + valor);
        }
        return true;
    }
}
