package net.byebye.balance;

public class BalanceAPI {

    private static Economy economy;

    // Isso é chamado na onEnable do seu plugin principal
    public static void init(Economy eco) {
        economy = eco;
    }

    public static Economy getEconomy() {
        return economy;
    }
}