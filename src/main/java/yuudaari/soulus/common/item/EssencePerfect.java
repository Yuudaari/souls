package yuudaari.soulus.common.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.google.common.collect.Streams;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import yuudaari.soulus.Soulus;
import yuudaari.soulus.client.util.Sneak;
import yuudaari.soulus.common.config.ConfigInjected;
import yuudaari.soulus.common.config.ConfigInjected.Inject;
import yuudaari.soulus.common.config.essence.ConfigEssences;
import yuudaari.soulus.common.recipe.ingredient.IngredientEssence;
import yuudaari.soulus.common.recipe.ingredient.IngredientEssence.AllowedStack;
import yuudaari.soulus.common.registration.ItemRegistry;
import yuudaari.soulus.common.registration.Registration;
import yuudaari.soulus.common.util.EssenceType;
import yuudaari.soulus.common.util.Translation;

@ConfigInjected(Soulus.MODID)
public class EssencePerfect extends Registration.Item {

	@Inject public static ConfigEssences CONFIG;

	@Nullable
	public static String[] getEssenceTypes (final ItemStack stack) {
		final NBTTagCompound tag = stack.getTagCompound();
		if (tag == null || !tag.hasKey("essence_types", 9))
			return new String[0];

		final NBTTagList list = tag.getTagList("essence_types", 8);
		return Streams.stream(list)
			.map(strTag -> ((NBTTagString) strTag).getString())
			.distinct()
			.toArray(String[]::new);
	}

	public static ItemStack setEssenceTypes (final ItemStack stack, final ResourceLocation... essenceTypes) {
		return setEssenceTypes(stack, Arrays.stream(essenceTypes)
			.map(rl -> rl.toString())
			.distinct()
			.toArray(String[]::new));
	}

	public static boolean isPerfect (final ItemStack stack) {
		final List<String> essenceTypesInStack = Arrays.stream(getEssenceTypes(stack))
			.sorted()
			.collect(Collectors.toList());
		final List<String> essenceTypes = CONFIG.getEssenceTypes()
			.sorted()
			.collect(Collectors.toList());
		return essenceTypes.equals(essenceTypesInStack);
	}

	public static ItemStack setEssenceTypes (final ItemStack stack, final String... essenceTypes) {

		final NBTTagList essenceTypesTag = new NBTTagList();
		Arrays.stream(essenceTypes)
			.distinct()
			.map(NBTTagString::new)
			.forEach(essenceTypesTag::appendTag);

		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}

		tag.setTag("essence_types", essenceTypesTag);

		return stack;
	}


	public static class EssencePerfectRecipe extends ShapelessOreRecipe {

		private static NonNullList<Ingredient> ingredients () {
			final List<Ingredient> ingredients = new ArrayList<>();
			ingredients.addAll(Collections.nCopies(3 * 3, IngredientEssence.getInstance(AllowedStack.EMPTY, AllowedStack.ESSENCE_PERFECT)));
			return NonNullList.from(Ingredient.EMPTY, ingredients.toArray(new Ingredient[0]));
		}

		public EssencePerfectRecipe (final ResourceLocation name) {
			super(new ResourceLocation(""), ingredients(), ItemRegistry.ESSENCE_PERFECT.getItemStack());
			setRegistryName(name);
		}

		@Override
		public boolean matches (final InventoryCrafting inv, final World worldIn) {
			return !getCraftingResult(inv).isEmpty();
		}

		@Override
		public ItemStack getCraftingResult (final InventoryCrafting inv) {

			final Set<String> essenceTypes = new HashSet<>();
			final int inventorySize = inv.getSizeInventory();
			int count = 0;

			for (int i = 0; i < inventorySize; i++) {
				final ItemStack stack = inv.getStackInSlot(i);
				final Item stackItem = stack.getItem();

				if (stack == null || stack.isEmpty())
					// empty slot, let's look at next
					continue;

				if (stackItem == ItemRegistry.ESSENCE)
					essenceTypes.add(EssenceType.getEssenceType(stack));

				else if (stackItem == ItemRegistry.ESSENCE_PERFECT)
					Collections.addAll(essenceTypes, getEssenceTypes(stack));

				else
					// some other random item
					return ItemStack.EMPTY;

				count++;
			}

			if (count < 2 || essenceTypes.size() < 2)
				// existential essence requires at least 2 essence types
				return ItemStack.EMPTY;

			return ItemRegistry.ESSENCE_PERFECT.getItemStack(count, essenceTypes.toArray(new String[0]));
		}
	}

	public EssencePerfect () {
		super("essence_perfect");
		setMaxStackSize(64);
		setHasDescription();

		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			registerColorHandler( (final ItemStack stack, final int tintIndex) -> {
				final String[] essenceTypes = getEssenceTypes(stack);
				if (essenceTypes.length == 0)
					return -1;

				// final String essenceType = essenceTypes[(int) (Math.random() * essenceTypes.length)];
				final String essenceType = essenceTypes[tintIndex % essenceTypes.length];
				return Essence.getColor(essenceType, tintIndex % 2);
			});
		}
	}

	public ItemStack getItemStack (final int count, final String... essenceTypes) {
		final ItemStack result = new ItemStack(ItemRegistry.ESSENCE_PERFECT);
		result.setCount(count);
		setEssenceTypes(result, essenceTypes);
		return result;
	}

	public ItemStack getPerfectStack () {
		return getPerfectStack(1);
	}

	public ItemStack getPerfectStack (final int count) {
		final ItemStack stack = getItemStack(count);
		setEssenceTypes(stack, CONFIG.getEssenceTypes().toArray(String[]::new));
		return stack;
	}

	@Override
	public void getSubItems (final CreativeTabs tab, final NonNullList<ItemStack> items) {
		if (!this.isInCreativeTab(tab)) return;
		items.add(getPerfectStack(1));
	}

	@Override
	public String getUnlocalizedNameInefficiently (final ItemStack stack) {
		String result = super.getUnlocalizedNameInefficiently(stack);
		return isPerfect(stack) ? result + ".complete" : result;
	}

	@Override
	public EnumRarity getRarity (final ItemStack stack) {
		return isPerfect(stack) ? EnumRarity.EPIC : EnumRarity.UNCOMMON;
	}

	// can't add glint due to a bug
	// @Override
	// public boolean hasEffect (final ItemStack stack) {
	// 	return isPerfect(stack);
	// }

	@Override
	public void onRegisterRecipes (final IForgeRegistry<IRecipe> registry) {
		registry.register(new EssencePerfectRecipe(getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation (final ItemStack stack, final World world, final List<String> tooltip, final ITooltipFlag flagIn) {
		final String registryName = getRegistryName().toString();
		final String[] essenceTypes = getEssenceTypes(stack);

		if (essenceTypes.length < 3 || Sneak.isSneaking()) {
			final StringBuilder builder = new StringBuilder(Translation.localize("tooltip." + registryName + ".alignments"));
			for (int i = 0; i < essenceTypes.length; i++) {
				builder.append(EssenceType.localize(essenceTypes[i]));
				if (i != essenceTypes.length - 1)
					builder.append(Translation.localize("tooltip." + registryName + (i == essenceTypes.length - 2 ? ".alignments_joining_last" : ".alignments_joining")));
			}

			tooltip.add(builder.toString());

		} else {
			tooltip.add(new Translation("tooltip." + registryName + ".alignments_count")
				.get(essenceTypes.length, CONFIG.getEssenceTypes().count()));
			tooltip.add(Translation.localize("tooltip." + registryName + ".sneak_to_show_more"));
		}
	}
}
