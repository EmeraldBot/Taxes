package com.mistphizzle.taxes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Taxes extends JavaPlugin {

	public static Economy ep;;

	protected static Logger log;

	public static Taxes instance;

	TaxCommand tc;

	private final TaxListener tL = new TaxListener(this);

	File configFile;
	FileConfiguration config;

	@Override
	public void onEnable() {
		setupEconomy();
		instance = this;
		this.log = this.getLogger();

		tc = new TaxCommand(this);
		configFile = new File(getDataFolder(), "config.yml");

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(tL, this);
		try {
			firstRun();
		} catch (Exception e) {
			e.printStackTrace();
		}

		config = new YamlConfiguration();
		loadYamls();
		
		try {
		    MetricsLite metrics = new MetricsLite(this);
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
		}
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

	public String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public void CollectTaxes(Player player) {
		String LastPaid = (String) getConfig().getString("players." + player.getName());
		String date = getCurrentDate();

		if (LastPaid == date) {
			player.sendMessage("LAST PAID EQUALS DATE");
			return;
		}

		if (player.hasPermission("taxes.exempt")) {
			return;
		}
		//		Set<String> playerList = plugin.getPlayerConfig().getConfigurationSection("players").getKeys(true);

		double balance = Taxes.ep.getBalance(player.getName());
		int low = getConfig().getInt("requirements.low");
		int middle = getConfig().getInt("requirements.middle");
		int high = getConfig().getInt("requirements.high");
		double lowtax = balance * getConfig().getDouble("taxbrackets.low");
		double middletax = balance * getConfig().getDouble("taxbrackets.middle");
		double hightax = balance * getConfig().getDouble("taxbrackets.high");
		double maxlowtax = getConfig().getDouble("maxtaxes.low");
		double maxmiddletax = getConfig().getDouble("maxtaxes.middle");
		double maxhightax = getConfig().getDouble("maxtaxes.high");
		String serveraccount = getConfig().getString("general.account");
		if (getConfig().get("players." + player.getName()) == null) {
			player.sendMessage("§cTaxes are due.");

			if (balance >= low && balance < middle) {
				String rounded = Taxes.ep.format(lowtax);
				if (lowtax > maxlowtax) {
					Taxes.ep.withdrawPlayer(player.getName(), maxlowtax);
					Taxes.ep.depositPlayer(serveraccount, maxlowtax);
					player.sendMessage("§3You just paid §6" + maxlowtax + " §3in taxes.");
				} else {
					Taxes.ep.withdrawPlayer(player.getName(), lowtax);
					Taxes.ep.depositPlayer(serveraccount, lowtax);
					player.sendMessage("§3You just paid §6" + rounded + " §3in taxes.");
				}
			}

			if (balance >= middle && balance < high) {
				String rounded = Taxes.ep.format(middletax);
				if (middletax > maxmiddletax) {
					Taxes.ep.withdrawPlayer(player.getName(), maxmiddletax);
					Taxes.ep.depositPlayer(serveraccount, maxmiddletax);
					player.sendMessage("§3You just paid §6" + maxmiddletax + " §3in taxes.");
				} else {
					Taxes.ep.withdrawPlayer(player.getName(), middletax);
					Taxes.ep.depositPlayer(serveraccount, middletax);
					player.sendMessage("§3You just paid §6" + rounded + " §3in taxes.");
				}
			}

			if (balance > high) {

				if (hightax > maxhightax) {
					Taxes.ep.withdrawPlayer(player.getName(), maxhightax);
					Taxes.ep.depositPlayer(serveraccount, maxhightax);
					player.sendMessage("§3You just paid §6" + maxhightax + " §3in taxes.");
				} else {
					String rounded = Taxes.ep.format(hightax);
					Taxes.ep.withdrawPlayer(player.getName(), hightax);
					Taxes.ep.depositPlayer(serveraccount, hightax);
					player.sendMessage("§3You just paid §6" + rounded + " §3in taxes.");
				}
			}

			getConfig().set("players." + player.getName(), date);
			saveConfig();

			return;
		}
		if (!getConfig().getString("players." + player.getName()).equals(date)) {
			player.sendMessage("§cTaxes are due.");

			if (balance >= low && balance < middle) {
				String rounded = Taxes.ep.format(lowtax);
				if (lowtax > maxlowtax) {
					Taxes.ep.withdrawPlayer(player.getName(), maxlowtax);
					Taxes.ep.depositPlayer(serveraccount, maxlowtax);
					player.sendMessage("§3You just paid §6" + maxlowtax + " §3in taxes.");
				} else {
					Taxes.ep.withdrawPlayer(player.getName(), lowtax);
					Taxes.ep.depositPlayer(serveraccount, lowtax);
					player.sendMessage("§3You just paid §6" + rounded + " §3in taxes.");
				}
			}

			if (balance >= middle && balance < high) {
				String rounded = Taxes.ep.format(middletax);
				if (middletax > maxmiddletax) {
					Taxes.ep.withdrawPlayer(player.getName(), maxmiddletax);
					Taxes.ep.depositPlayer(serveraccount, maxmiddletax);
					player.sendMessage("§3You just paid §6" + maxmiddletax + " §3in taxes.");
				} else {
					Taxes.ep.withdrawPlayer(player.getName(), middletax);
					Taxes.ep.depositPlayer(serveraccount, middletax);
					player.sendMessage("§3You just paid §6" + rounded + " §3in taxes.");
				}
			}

			if (balance > high) {

				if (hightax > maxhightax) {
					Taxes.ep.withdrawPlayer(player.getName(), maxhightax);
					Taxes.ep.depositPlayer(serveraccount, maxhightax);
					player.sendMessage("§3You just paid §6" + maxhightax + " §3in taxes.");
				} else {
					String rounded = Taxes.ep.format(hightax);
					Taxes.ep.withdrawPlayer(player.getName(), hightax);
					Taxes.ep.depositPlayer(serveraccount, hightax);
					player.sendMessage("§3You just paid §6" + rounded + " §3in taxes.");
				}
			}

			getConfig().set("players." + player.getName(), date);
			saveConfig();

			return;
		}
	}
}
