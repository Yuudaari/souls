package yuudaari.soulus.common.config.block;

import yuudaari.soulus.common.block.composer.Composer;
import yuudaari.soulus.common.block.upgradeable_block.UpgradeableBlock.IUpgrade;
import yuudaari.soulus.common.config.ClientField;
import yuudaari.soulus.common.config.ConfigFile;
import yuudaari.soulus.common.util.Range;
import yuudaari.soulus.common.util.serializer.CollectionSerializer;
import yuudaari.soulus.common.util.serializer.NullableField;
import yuudaari.soulus.common.util.serializer.Serializable;
import yuudaari.soulus.common.util.serializer.Serialized;
import java.util.Set;
import yuudaari.soulus.Soulus;

@ConfigFile(file = "block/composer", id = Soulus.MODID)
@Serializable
public class ConfigComposer extends ConfigUpgradeableBlock<Composer> {

	@Override
	protected IUpgrade[] getUpgrades () {
		return Composer.Upgrade.values();
	}

	// CLIENT
	@Serialized @ClientField public double particleCountActivated = 1;
	@Serialized @ClientField public int particleCountMax = 6;
	@Serialized @ClientField public int particleCountMobPoof = 50;

	// SERVER
	@Serialized public Range nonUpgradedDelay = new Range(500, 1000);
	@Serialized public int nonUpgradedRange = 4;
	@Serialized public Range upgradeDelayEffectiveness = new Range(0.8, 1);
	@Serialized public int upgradeRangeEffectiveness = 1;
	@Serialized public Range poofChance = new Range(0.005, 0.0005);
	@Serialized public Consumption consume = new Consumption();
	@Serialized(CollectionSerializer.OfStrings.class) @NullableField public Set<String> whitelistedCreatures;
	@Serialized(CollectionSerializer.OfStrings.class) @NullableField public Set<String> blacklistedCreatures;

	@Serializable
	public static class Consumption {

		@Serialized public boolean natural = false;
		@Serialized public boolean malicious = true;
		@Serialized public boolean summoned = true;
		@Serialized public boolean spawnedFromEgg = false;
		@Serialized public boolean named = false;
		@Serialized public boolean tamed = true;
	}
}
