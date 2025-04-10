package net.byebye.balance;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Economy {

    public static final HashMap<UUID, Double> playerSaldos = new HashMap<>();
    private final File balancesFile = new File(Main.m.getDataFolder(), "balances.yml");
    private final FileConfiguration balancesConfig = YamlConfiguration.loadConfiguration(balancesFile);

    public Economy() {
        loadSaldos();
    }

    public void addSaldo(OfflinePlayer target, double amount) {
        UUID uuid = target.getUniqueId();
        playerSaldos.put(uuid, getSaldo(target) + amount);
        saveSaldo(target);
    }

    // Remove saldo
    public void removeSaldo(OfflinePlayer target, double amount) {
        UUID uuid = target.getUniqueId();
        double newBalance = Math.max(0, getSaldo(target) - amount);
        playerSaldos.put(uuid, newBalance);
        saveSaldo(target);
    }

    // Define saldo
    public void setSaldo(OfflinePlayer target, double amount) {
        UUID uuid = target.getUniqueId();
        playerSaldos.put(uuid, amount);
        saveSaldo(target);
    }

    // Pega o saldo atual
    public double getSaldo(OfflinePlayer target) {
        UUID uuid = target.getUniqueId();
        return playerSaldos.getOrDefault(uuid, 0.0);
    }

    // Salva o saldo do jogador na balances.yml
    public void saveSaldo(OfflinePlayer player) {
        String key = player.getName() + "." + player.getUniqueId();
        balancesConfig.set(key, getSaldo(player));
        try {
            balancesConfig.save(balancesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carrega todos os saldos do balances.yml
    public void loadSaldos() {
        for (String playerName : balancesConfig.getKeys(false)) {
            ConfigurationSection section = balancesConfig.getConfigurationSection(playerName);
            if (section == null) continue;

            for (String uuidStr : section.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    double saldo = section.getDouble(uuidStr);
                    playerSaldos.put(uuid, saldo);
                } catch (IllegalArgumentException e) {
                    Main.m.getLogger().warning("UUID inválido em balances.yml: " + uuidStr);
                }
            }
        }
    }
}
