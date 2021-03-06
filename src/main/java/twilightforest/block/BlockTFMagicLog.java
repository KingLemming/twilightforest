package twilightforest.block;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.enums.MagicWoodVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

import java.util.Random;

public class BlockTFMagicLog extends BlockLog implements ModelRegisterCallback {

	public static final PropertyEnum<MagicWoodVariant> VARIANT = PropertyEnum.create("variant", MagicWoodVariant.class);

	protected BlockTFMagicLog() {
		this.setHardness(2.0F);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(blockState.getBaseState().withProperty(VARIANT, MagicWoodVariant.TIME).withProperty(LOG_AXIS, EnumAxis.Y));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT, LOG_AXIS);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, MagicWoodVariant.values()[meta & 3]);

		switch (meta & 0b1100) {
			case 0:
				iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
				break;
			case 4:
				iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
				break;
			case 8:
				iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
				break;
			default:
				iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
		}

		return iblockstate;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(VARIANT).ordinal();

		switch (state.getValue(LOG_AXIS)) {
			case X:
				i |= 4;
				break;
			case Y:
				i |= 0;
				break;
			case Z:
				i |= 8;
				break;
			case NONE:
				i |= 12;
		}

		return i;
	}

	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return false;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	public void getSubBlocks(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> par3List) {
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 1));
		par3List.add(new ItemStack(this, 1, 2));
		par3List.add(new ItemStack(this, 1, 3));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToStateSingleVariant(this, VARIANT);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(state.getBlock(), 1, state.getValue(VARIANT).ordinal());
	}
}
