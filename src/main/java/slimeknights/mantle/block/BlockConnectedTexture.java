package slimeknights.mantle.block;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

 /**
  * Creates a block with textures that connect to other blocks
  * <p>
  * Based off a tutorial by Darkhax, used under the Creative Commons Zero 1.0 Universal license
  */
public class BlockConnectedTexture extends Block {
    
    // These are the properties used for determining whether or not a side is connected. They
    // do NOT take up block IDs, they are unlisted properties
    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create(("connected_down");
    public static final BooleanProperty CONNECTED_UP = BooleanProperty.create(("connected_up");
    public static final BooleanProperty CONNECTED_NORTH = BooleanProperty.create(("connected_north");
    public static final BooleanProperty CONNECTED_SOUTH = BooleanProperty.create(("connected_south");
    public static final BooleanProperty CONNECTED_WEST = BooleanProperty.create(("connected_west");
    public static final BooleanProperty CONNECTED_EAST = BooleanProperty.create(("connected_east");
    
    public BlockConnectedTexture(Material material) {
        
      super(material);
      // By default none of the sides are connected
      this.setDefaultState(this.stateContainer.getBaseState()
                                          .with(CONNECTED_DOWN, Boolean.FALSE)
                                          .with(CONNECTED_EAST, Boolean.FALSE)
                                          .with(CONNECTED_NORTH, Boolean.FALSE)
                                          .with(CONNECTED_SOUTH, Boolean.FALSE)
                                          .with(CONNECTED_UP, Boolean.FALSE)
                                          .with(CONNECTED_WEST, Boolean.FALSE));
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos position) {
        
      // Creates the state to use for the block. This is where we check if every side is
      // connectable or not.
      return state.withProperty(CONNECTED_DOWN,  this.isSideConnectable(state, world, position, EnumFacing.DOWN))
                  .withProperty(CONNECTED_EAST,  this.isSideConnectable(state, world, position, EnumFacing.EAST))
                  .withProperty(CONNECTED_NORTH, this.isSideConnectable(state, world, position, EnumFacing.NORTH))
                  .withProperty(CONNECTED_SOUTH, this.isSideConnectable(state, world, position, EnumFacing.SOUTH))
                  .withProperty(CONNECTED_UP,    this.isSideConnectable(state, world, position, EnumFacing.UP))
                  .withProperty(CONNECTED_WEST,  this.isSideConnectable(state, world, position, EnumFacing.WEST));
    }

     @Override
     protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
         builder.add(CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST);
     }
    
    // Since the block has state information but we are not switching the meta value, we have
    // to override this method to return 0
    @Override
    public int getMetaFromState(IBlockState state) {
      return 0;
    }
    
    /**
     * Checks if a specific side of a block can connect to this block. For this example, a side
     * is connectable if the block is the same block as this one.
     *
     * @param state Base state to check
     * @param world The world to run the check in.
     * @param pos The position of the block to check for.
     * @param side The side of the block to check.
     * @return Whether or not the side is connectable.
     */
    private boolean isSideConnectable(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
      final IBlockState connected = world.getBlockState(pos.offset(side));
      return connected != null && canConnect(state, connected);
    }
    
    /**
     * Checks if this block should connect to another block
     * @param state BlockState to check
     * @return True if the block is valid to connect
     */
    protected boolean canConnect(@Nonnull IBlockState original, @Nonnull IBlockState connected) {
      return original.getBlock() == connected.getBlock();
    }
}