/*
 * Copyright (c) 2018, Infinitay <https://github.com/Infinitay>
 * Copyright (c) 2019, Parker <https://github.com/Judaxx>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.kingdomofmiscellania;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.inject.Singleton;
import net.runelite.client.ui.overlay.infobox.Counter;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.QuantityFormatter;

@Singleton
class KingdomCounter extends Counter
{
	private final KingdomPlugin plugin;

	KingdomCounter(final BufferedImage image, final KingdomPlugin plugin)
	{
		super(image, plugin, plugin.getFavor());
		this.plugin = plugin;
	}

	private String bestResources()
	{
		Kingdom maxKingdom = plugin.getMaxKingdom();

		if (maxKingdom.resourceProfit == null)
		{
			return ColorUtil.wrapWithColorTag("Calculating resource profits...", Color.ORANGE);
		}

		StringBuilder maxRewards = new StringBuilder(ColorUtil.wrapWithColorTag("Resource Yields </br>", Color.YELLOW));

		for (Map.Entry<ResourceType, Integer> entry : maxKingdom.resourceProfit.entrySet())
		{
			maxRewards.append(entry.getKey().getType()).append(": ").append(QuantityFormatter.formatNumber(entry.getValue())).append("</br>");
		}
		return maxRewards.toString();
	}

	private String rewardInfo()
	{
		Kingdom personalKingdom = plugin.getPersonalKingdom();

		if (personalKingdom.resourceProfit == null)
		{
			return ColorUtil.wrapWithColorTag("Calculating reward info...", Color.ORANGE);
		}

		StringBuilder currentRewardSettings = new StringBuilder(ColorUtil.wrapWithColorTag("Current Rewards: ", Color.YELLOW)
			+ QuantityFormatter.formatNumber(personalKingdom.grossProfit) + "</br>");

		for (Map.Entry<Reward, Integer> entry : personalKingdom.rewardQuantity.entrySet())
		{
			currentRewardSettings.append(entry.getKey().getName()).append(" x ").append(QuantityFormatter.formatNumber(entry.getValue())).append("</br>");
		}
		return currentRewardSettings.toString();
	}

	@Override
	public String getText()
	{
		return KingdomPlugin.getFavorPercent(plugin.getFavor()) + "%";
	}

	@Override
	public String getTooltip()
	{
		String kingdomSummary = ColorUtil.wrapWithColorTag("Favor: ", Color.YELLOW)
			+ KingdomPlugin.getFavorPercent(plugin.getFavor()) + " / 100" + "</br>"
			+ ColorUtil.wrapWithColorTag("Coffer: ", Color.YELLOW)
			+ QuantityFormatter.quantityToRSDecimalStack(plugin.getCoffer()) + "</br>";

		return kingdomSummary + bestResources() + rewardInfo();
	}
}
