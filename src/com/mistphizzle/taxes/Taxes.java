package com.mistphizzle.taxes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Taxes extends JavaPlugin {
	
	public static Economy ep;;
	
	protected static Logger log;
	
	public static Taxes instance;
	
	TaxCommand tc;
	
	File configFile;
	FileConfiguration config;

	@Override
	public void onEnable() {
		setupEconomy();
		instance = this;
		this.log = this.getLogger();
		
		tc = new TaxCommand(this);
		configFile = new File(getDataFolder(), "config.yml");
		
		try {
			firstRun();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		config = new YamlConfiguration();
		loadYamls();
	}
	
	@Override
	public void onDisable() {
		//
	}
	
	// Methods
	
	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		ep = economyProvider.getProvider();
		return (ep != null);
	}
	
	public void firstRun() throws Exception {
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			copy(getResource("config.yml"), configFile);
			log.info("Config not found, Generating.");
		}
	}
	
	private void loadYamls() {
		try {
			config.load(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf))>0) {
				out.write(buf,0,len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveYamls() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Taxes getInstance() {
		return instance;
	}
	
	public void CollectTaxes() {
		for(Player player: Bukkit.getOnlinePlayers()) {
			Bukkit.broadcastMessage("§4Taxes are due. Collecting.");
			if (player.hasPermission("taxes.exempt")) {
				player.sendMessage("§cYou are exempt from taxes.");
			}
			if (!player.hasPermission("taxes.exempt")) {
				double balance = Taxes.ep.getBalance(player.getName());
				int low = getConfig().getInt("requirements.low");
				int middle = getConfig().getInt("requirements.middle");
				int high = getConfig().getInt("requirements.high");
				double lowtax = balance * getConfig().getDouble("taxbrackets.low");
				double middletax = balance * getConfig().getDouble("taxbrackets.middle");
				double hightax = balance * getConfig().getDouble("taxbrackets.high");
				String serveraccount = getConfig().getString("general.account");
				
				if (balance >= low && balance < middle) {
					String rounded = ep.format(lowtax);
					ep.withdrawPlayer(player.getName(), lowtax);
					ep.depositPlayer(serveraccount, lowtax);
					player.sendMessage("§3You just paid §6" + rounded + " §3in taxes.");
				}
				
				if (balance >= middle && balance < high) {
					String rounded = ep.format(middletax);
					ep.withdrawPlayer(player.getName(), middletax);
					ep.depositPlayer(serveraccount, middletax);
					player.sendMessage("§3You just paid §6" + rounded + " §3in taxes.");
				}
				
				if (balance >= high) {
					String rounded = ep.format(hightax);
					ep.withdrawPlayer(player.getName(), hightax);
					ep.depositPlayer(serveraccount, hightax);
					player.sendMessage("§3You just paid §6" + rounded + " §3in taxes.");
				}
			}
		}
	}
	
}
