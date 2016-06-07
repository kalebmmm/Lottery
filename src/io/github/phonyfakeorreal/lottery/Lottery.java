package io.github.phonyfakeorreal.lottery;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Lottery extends JavaPlugin {

	LotteryConfiguration config;
	public static Economy econ = null;
	
	public void onEnable()
	{
		saveDefaultConfig();
		
		config = new LotteryConfiguration(this);
		
		if (Bukkit.getPluginManager().getPlugin("Vault") == null)
		{
			this.getLogger().log(Level.WARNING, "Vault could not be found! Shutting down!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		else
		{
			System.out.println(setupEconomy());
		}
		
		new LotteryListener(this, econ, new LotteryConfiguration(this));
	}
	
	private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
