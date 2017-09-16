package yuudaari.souls.common.util;

import yuudaari.souls.Souls;
import yuudaari.souls.common.CreativeTab;
import yuudaari.souls.common.recipe.Recipe;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;

import javax.annotation.Nonnull;

public class ModBlock extends Block implements IBlock {
	private String name;
	private List<String> oreDicts = new ArrayList<>();
	private Boolean hasItem = false;
	private ItemBlock itemBlock;

	public ModBlock(String name, Material material) {
		super(material);
		setName(name);
		setCreativeTab(CreativeTab.INSTANCE);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setRegistryName(Souls.MODID, name);
		setUnlocalizedName(getRegistryName().toString());
	}

	public void addOreDict(String name) {
		oreDicts.add(name);
	}

	public List<String> getOreDicts() {
		return oreDicts;
	}

	protected List<Recipe> recipes = new ArrayList<>();

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void addRecipe(Recipe recipe) {
		recipes.add(recipe);
	}

	public void setHasItem() {
		if (!hasItem) {
			hasItem = true;
			itemBlock = new ItemBlock(this);
			itemBlock.setRegistryName(getRegistryName());
		}
	}

	public boolean hasItem() {
		return hasItem;
	}

	public ItemBlock getItemBlock() {
		if (!hasItem)
			throw new IllegalArgumentException("This block has no registered item");
		return itemBlock;
	}

	public Class<? extends TileEntity> getTileEntityClass() {
		return null;
	}

	@Nonnull
	@Override
	public BlockRenderLayer getBlockLayer() {
		Material material = blockMaterial;
		if (material.isOpaque())
			return BlockRenderLayer.SOLID;
		else if (material.blocksLight())
			return BlockRenderLayer.TRANSLUCENT;
		else
			return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return blockMaterial.isOpaque() && blockMaterial.blocksLight();
	}
}