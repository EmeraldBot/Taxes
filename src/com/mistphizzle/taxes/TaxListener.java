package com.mistphizzle.taxes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TaxListener implements Listener {

	public static Taxes plugin;

	public TaxListener(Taxes instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent e) {
		Player player = e.getPlayer();
		String LastPaid = (String) plugin.getConfig().getString("players." + player.getName());
		String date = plugin.getCurrentDate();

		if (LastPaid == date) {
			player.sendMessage("LAST PAID EQUALS DATE");
			return;
		}

		if (player.hasPermission("taxes.exempt")) {
			return;
		}
		//		Set<String> playerList = plugin.getPlayerConfig().getConfigurationSection("players").getKeys(true);

		double balance = Taxes.ep.getBalance(player.getName());
		int low = plugin.getConfig().getInt("requirements.low");
		int middle = plugin.getConfig().getInt("requirements.middle");
		int high = plugin.getConfig().getInt("requirements.high");
		double lowtax = balance * plugin.getConfig().getDouble("taxbrackets.low");
		double middletax = balance * plugin.getConfig().getDouble("taxbrackets.middle");
		double hightax = balance * plugin.getConfig().getDouble("taxbrackets.high");
		double maxlowtax = plugin.getConfig().getDouble("maxtaxes.low");
		double maxmiddletax = plugin.getConfig().getDouble("maxtaxes.middle");
		double maxhightax = plugin.getConfig().getDouble("maxtaxes.high");
		String serveraccount = plugin.getConfig().getString("general.account");
		if (plugin.getConfig().get("players." + player.getName()) == null) {
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

			plugin.getConfig().set("players." + player.getName(), date);
			plugin.saveConfig();

			return;
		}
		if (!plugin.getConfig().getString("players." + player.getName()).equals(date)) {
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

			plugin.getConfig().set("players." + player.getName(), date);
			plugin.saveConfig();

			return;
		}
	}
}
