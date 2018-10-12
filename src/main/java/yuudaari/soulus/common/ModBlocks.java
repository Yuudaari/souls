package yuudaari.soulus.common;

import yuudaari.soulus.common.util.IBlock;
import yuudaari.soulus.common.util.IModThing;
import yuudaari.soulus.common.util.IProvidesJeiDescription;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import yuudaari.soulus.common.block.*;
import yuudaari.soulus.common.block.composer.*;
import yuudaari.soulus.common.block.enderlink.Enderlink;
import yuudaari.soulus.common.block.summoner.Summoner;
import yuudaari.soulus.common.compat.JeiDescriptionRegistry;
import yuudaari.soulus.common.block.skewer.Skewer;
import yuudaari.soulus.common.block.soul_totem.SoulTotem;

public class ModBlocks {

	public static final AshBlock ASH = new AshBlock();
	public static final BarsEndersteel BARS_ENDERSTEEL = new BarsEndersteel();
	public static final BlockEndersteel BLOCK_ENDERSTEEL = new BlockEndersteel();
	public static final BlockEndersteelDark BLOCK_ENDERSTEEL_DARK = new BlockEndersteelDark();
	public static final BlockNiobium BLOCK_NIOBIUM = new BlockNiobium();
	public static final Composer COMPOSER = new Composer();
	public static final ComposerCell COMPOSER_CELL = new ComposerCell();
	public static final DustEnderBlock DUST_ENDER = new DustEnderBlock();
	public static final Enderlink ENDERLINK = new Enderlink();
	public static final FossilDirt FOSSIL_DIRT = new FossilDirt();
	public static final FossilDirtEnder FOSSIL_DIRT_ENDER = new FossilDirtEnder();
	public static final FossilDirtFrozen FOSSIL_DIRT_FROZEN = new FossilDirtFrozen();
	public static final FossilDirtFungal FOSSIL_DIRT_FUNGAL = new FossilDirtFungal();
	public static final FossilEndStone FOSSIL_END_STONE = new FossilEndStone();
	public static final FossilGravel FOSSIL_GRAVEL = new FossilGravel();
	public static final FossilNetherrack FOSSIL_NETHERRACK = new FossilNetherrack();
	public static final FossilNetherrackEnder FOSSIL_NETHERRACK_ENDER = new FossilNetherrackEnder();
	public static final FossilSand FOSSIL_SAND = new FossilSand();
	public static final FossilSandEnder FOSSIL_SAND_ENDER = new FossilSandEnder();
	public static final FossilSandScale FOSSIL_SAND_SCALE = new FossilSandScale();
	public static final FossilSandRed FOSSIL_SAND_RED = new FossilSandRed();
	public static final FossilSandRedScale FOSSIL_SAND_RED_SCALE = new FossilSandRedScale();
	public static final FossilIce FOSSIL_ICE = new FossilIce();
	public static final FossilIceEnder FOSSIL_ICE_ENDER = new FossilIceEnder();
	public static final FossilIceFrozen FOSSIL_ICE_FROZEN = new FossilIceFrozen();
	public static final FossilIceScale FOSSIL_ICE_SCALE = new FossilIceScale();
	public static final Skewer SKEWER = new Skewer();
	public static final Summoner SUMMONER = new Summoner();
	public static final Unloader UNLOADER = new Unloader();
	public static final SoulTotem SOUL_TOTEM = new SoulTotem();

	public static IBlock[] blocks = new IBlock[] {
		DUST_ENDER,

		FOSSIL_DIRT,
		FOSSIL_DIRT_FROZEN,
		FOSSIL_DIRT_FUNGAL,
		FOSSIL_DIRT_ENDER,

		FOSSIL_SAND,
		FOSSIL_SAND_SCALE,
		FOSSIL_SAND_ENDER,
		FOSSIL_SAND_RED,
		FOSSIL_SAND_RED_SCALE,

		FOSSIL_ICE,
		FOSSIL_ICE_ENDER,
		FOSSIL_ICE_FROZEN,
		FOSSIL_ICE_SCALE,

		FOSSIL_GRAVEL,

		FOSSIL_NETHERRACK,
		FOSSIL_NETHERRACK_ENDER,

		FOSSIL_END_STONE,

		ASH,

		BARS_ENDERSTEEL,

		BLOCK_ENDERSTEEL,
		BLOCK_ENDERSTEEL_DARK,
		BLOCK_NIOBIUM,

		SKEWER,

		UNLOADER,

		SUMMONER,

		COMPOSER,
		COMPOSER_CELL,

		ENDERLINK,

		SOUL_TOTEM
	};

	public static void registerBlocks (IForgeRegistry<Block> registry) {
		for (IBlock block : blocks) {
			registry.register((Block) block);
		}
	}

	public static void registerItems (IForgeRegistry<Item> registry) {
		for (IBlock block : blocks) {
			if (block.hasItem()) {
				for (ItemBlock item : block.getItemBlocks()) {
					registry.register(item);
					for (String dict : block.getOreDicts()) {
						OreDictionary.registerOre(dict, item);
					}
				}
			}

			Class<? extends TileEntity> te = block.getTileEntityClass();
			if (te != null) {
				GameRegistry.registerTileEntity(te, block.getRegistryName().toString());
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static void registerModels () {
		for (IBlock block : blocks) {
			if (block.hasItem()) {
				block.registerItemModel();
			}
		}
	}

	public static void registerRecipes (IForgeRegistry<IRecipe> registry) {
		for (IBlock block : blocks) {
			if (block instanceof IModThing)
				((IModThing) block).onRegisterRecipes(registry);
		}
	}

	public static void registerDescriptions (JeiDescriptionRegistry registry) {
		for (IBlock block : blocks) {
			if (block instanceof IProvidesJeiDescription)
				((IProvidesJeiDescription) block).onRegisterDescription(registry);
		}
	}
}
