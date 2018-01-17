package yuudaari.soulus.common.config.creature;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonElement;
import yuudaari.soulus.common.util.serializer.DefaultMapSerializer;
import yuudaari.soulus.common.util.serializer.Serializable;
import yuudaari.soulus.common.util.serializer.Serialized;

@Serializable
public class ConfigCreatureDimension {

	public String dimensionId;
	@Serialized(value = BiomeMapSerializer.class, topLevel = true) public Map<String, ConfigCreatureBiome> biomeConfigs = new HashMap<>();

	public static class BiomeMapSerializer extends DefaultMapSerializer<ConfigCreatureBiome> {

		@Override
		public Class<ConfigCreatureBiome> getValueClass () {
			return ConfigCreatureBiome.class;
		}

		@Override
		public Map<String, ConfigCreatureBiome> deserialize (final Class<?> requestedType, final JsonElement json) {
			final Map<String, ConfigCreatureBiome> result = super.deserialize(requestedType, json);
			if (result == null) return result;

			for (final Map.Entry<String, ConfigCreatureBiome> entry : result.entrySet()) {
				entry.getValue().biomeId = entry.getKey();
			}

			return result;
		}
	}

	public ConfigCreatureDimension () {}

	public ConfigCreatureDimension (final Map<String, ConfigCreatureBiome> creatureConfigs) {
		this.biomeConfigs = creatureConfigs;

		for (final Map.Entry<String, ConfigCreatureBiome> entry : creatureConfigs.entrySet()) {
			entry.getValue().biomeId = entry.getKey();
		}
	}
}