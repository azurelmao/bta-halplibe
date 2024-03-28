package turniplabs.halplibe.helper;

import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFire;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.sound.BlockSound;
import net.minecraft.core.sound.BlockSoundDispatcher;
import net.minecraft.core.util.helper.Side;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BlockBuilder implements Cloneable {
    private final String MOD_ID;
    private Float hardness = null;
    private Float resistance = null;
    private Integer luminance = null;
    private Integer lightOpacity = null;
    private Float slipperiness = null;
    private boolean immovable = false;
    private boolean useInternalLight = false;
    private boolean visualUpdateOnMetadata = false;
    private Boolean tickOnLoad = null;
    private boolean infiniburn = false;
    private int[] flammability = null;
    private Block blockDrop = null;
    private BlockSound blockSound = null;
    private BlockColor blockColor = null;
    private BlockModel blockModel = null;
    private BlockLambda<ItemBlock> customItemBlock = null;
    private Tag<Block>[] tags = null;
    private int[] topTexture = null;
    private int[] bottomTexture = null;
    private int[] northTexture = null;
    private int[] eastTexture = null;
    private int[] southTexture = null;
    private int[] westTexture = null;

    public BlockBuilder(String modId) {
        MOD_ID = modId;
    }

    @Override
    public BlockBuilder clone() {
        try {
            // none of the fields are mutated so this should be fine
            return (BlockBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Sets how long it takes to break the block.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setHardness(float hardness) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.hardness = hardness;
        return blockBuilder;
    }

    /**
     * Sets the block's resistance against explosions.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setResistance(float resistance) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.resistance = resistance;
        return blockBuilder;
    }

    /**
     * Sets the block's light emitting capacity.
     *
     * @param luminance ranges from 0 to 15
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setLuminance(int luminance) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.luminance = luminance;
        return blockBuilder;
    }

    /**
     * Sets the block's ability for light to pass through it.<br>
     * Block light and sunlight (once it encounters a non-transparent block) decreases
     * its intensity by 1 every block travelled.<br>
     * Therefore, when passing through a block with opacity 1, it will actually decrease by 2.
     *
     * @param lightOpacity ranges from 0 to 15
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setLightOpacity(int lightOpacity) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.lightOpacity = lightOpacity;
        return blockBuilder;
    }

    /**
     * Sets the block's slipperiness, 0.6 is default, 0.98 is ice.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setSlipperiness(float slipperiness) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.slipperiness = slipperiness;
        return blockBuilder;
    }

    /**
     * Sets the block's flammability.
     *
     * @param chanceToCatchFire how likely it is for the block to catch fire
     *                          non-destructively
     * @param chanceToDegrade   how likely it is for the block to burn itself
     *                          to ash and disappear
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setFlammability(int chanceToCatchFire, int chanceToDegrade) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.flammability = new int[]{chanceToCatchFire, chanceToDegrade};
        return blockBuilder;
    }

    /**
     * Makes a block unable to be moved by pistons.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setImmovable() {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.immovable = true;
        return blockBuilder;
    }

    /**
     * Makes a block unable to be broken.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setUnbreakable() {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.hardness = -1.0f;
        return blockBuilder;
    }

    /**
     * Makes fire burn indefinitely on top of the block.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setInfiniburn() {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.infiniburn = true;
        return blockBuilder;
    }

    /**
     * Makes a block's interior faces get light from the block's position.<br>
     * Used for things like slabs, stairs, layers and various other non-full
     * blocks that allow light to pass through them.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setUseInternalLight() {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.useInternalLight = true;
        return blockBuilder;
    }

    /**
     * Makes the block receive a visual update when the metadata of that block changes.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setVisualUpdateOnMetadata() {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.visualUpdateOnMetadata = true;
        return blockBuilder;
    }

    /**
     * Makes the block receive a tick update when the game loads the chunk the block is in.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setTickOnLoad() {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.tickOnLoad = true;
        return blockBuilder;
    }
    /**
     * Makes the block receive a tick update when the game loads the chunk the block is in.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setTicking(boolean ticking) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.tickOnLoad = ticking;
        return blockBuilder;
    }

    /**
     * Makes a block drop a different block than itself upon breaking.
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setBlockDrop(Block droppedBlock) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.blockDrop = droppedBlock;
        return blockBuilder;
    }

    /**
     * Sets the block's sound when walking over and breaking it.<br>
     * Example code:
     * <pre>{@code
     *     public static final Block exampleBlock = new BlockBuilder(MOD_ID)
     *          .setBlockSound(BlockSounds.WOOD)
     *          .build(new Block("example.block", 4000, Material.wood));
     * }</pre>
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setBlockSound(BlockSound blockSound) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.blockSound = blockSound;
        return blockBuilder;
    }

    /**
     * Makes the block's textures be colorized according to the provided BlockColor.<br>
     * Example code:
     * <pre>{@code
     *     public static final Block customGrassBlock = new BlockBuilder(MOD_ID)
     *          .setBlockColor(new BlockColorGrass())
     *          .build(new BlockGrass("custom.grass.block", 4001, Material.grass));
     * }</pre>
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setBlockColor(BlockColor blockColor) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.blockColor = blockColor;
        return blockBuilder;
    }

    /**
     * Sets the block's visible model.<br>
     * Example code:
     * <pre>{@code
     *     public static final Block customFlower = new BlockBuilder(MOD_ID)
     *          .setBlockModel(new BlockModelRenderBlocks(1))
     *          .build(new BlockFlower("custom.flower", 4002);
     * }</pre>
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setBlockModel(BlockModel blockModel) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.blockModel = blockModel;
        return blockBuilder;
    }

    /**
     * Sets the block's item used to place the block.<br>
     * Example code:
     * <pre>{@code
     *     public static final Block customSlab = new BlockBuilder(MOD_ID)
     *          .setItemBlock(ItemBlockSlab::new)
     *          .build(new BlockSlab(Block.dirt, 4003));
     * }</pre>
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setItemBlock(BlockLambda<ItemBlock> customItemBlock) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.customItemBlock = customItemBlock;
        return blockBuilder;
    }

    @SafeVarargs
    @SuppressWarnings({"unused"})
    public final BlockBuilder setTags(Tag<Block>... tags) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.tags = tags;
        return blockBuilder;
    }

    @SafeVarargs
    @SuppressWarnings({"unused"})
    public final BlockBuilder addTags(Tag<Block>... tags) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.tags = ArrayUtils.addAll(this.tags, tags);
        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setTextures(String texture) {
        return setTextures(MOD_ID, texture);
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setTextures(String modID, String texture) {
        int[] one = TextureHelper.getOrCreateBlockTexture(modID, texture);

        BlockBuilder blockBuilder = this.clone();
        blockBuilder.topTexture = one;
        blockBuilder.bottomTexture = one;
        blockBuilder.northTexture = one;
        blockBuilder.eastTexture = one;
        blockBuilder.southTexture = one;
        blockBuilder.westTexture = one;

        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setTextures(int x, int y) {
        int[] one = new int[]{x, y};

        BlockBuilder blockBuilder = this.clone();
        blockBuilder.topTexture = one;
        blockBuilder.bottomTexture = one;
        blockBuilder.northTexture = one;
        blockBuilder.eastTexture = one;
        blockBuilder.southTexture = one;
        blockBuilder.westTexture = one;

        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setSideTextures(String texture) {
        return setSideTextures(MOD_ID, texture);
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setSideTextures(String modID, String texture) {
        int[] sides = TextureHelper.getOrCreateBlockTexture(modID, texture);

        BlockBuilder blockBuilder = this.clone();
        blockBuilder.northTexture = sides;
        blockBuilder.eastTexture = sides;
        blockBuilder.southTexture = sides;
        blockBuilder.westTexture = sides;

        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setSideTextures(int x, int y) {
        int[] sides = new int[]{x, y};

        BlockBuilder blockBuilder = this.clone();
        blockBuilder.northTexture = sides;
        blockBuilder.eastTexture = sides;
        blockBuilder.southTexture = sides;
        blockBuilder.westTexture = sides;

        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setTopBottomTexture(String texture) {
        return setTopBottomTexture(MOD_ID, texture);
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setTopBottomTexture(String modID, String texture) {
        int[] topBottom = TextureHelper.getOrCreateBlockTexture(modID, texture);

        BlockBuilder blockBuilder = this.clone();
        blockBuilder.topTexture = topBottom;
        blockBuilder.bottomTexture = topBottom;

        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setTopBottomTexture(int x, int y) {
        int[] topBottom = new int[]{x, y};

        BlockBuilder blockBuilder = this.clone();
        blockBuilder.topTexture = topBottom;
        blockBuilder.bottomTexture = topBottom;

        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setTopTexture(String texture) {
        return setTopTexture(MOD_ID, texture);
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setTopTexture(String modID, String texture) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.topTexture = TextureHelper.getOrCreateBlockTexture(modID, texture);
        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setTopTexture(int x, int y) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.topTexture = new int[]{x, y};
        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setBottomTexture(String texture) {
        return setBottomTexture(MOD_ID, texture);
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setBottomTexture(String modID, String texture) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.bottomTexture = TextureHelper.getOrCreateBlockTexture(modID, texture);
        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setBottomTexture(int x, int y) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.bottomTexture = new int[]{x, y};
        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setNorthTexture(String texture) {
        return setNorthTexture(MOD_ID, texture);
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setNorthTexture(String modID, String texture) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.northTexture = TextureHelper.getOrCreateBlockTexture(modID, texture);
        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setNorthTexture(int x, int y) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.northTexture = new int[]{x, y};
        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setEastTexture(String texture) {
        return setEastTexture(MOD_ID, texture);
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setEastTexture(String modID, String texture) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.eastTexture = TextureHelper.getOrCreateBlockTexture(modID, texture);
        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setEastTexture(int x, int y) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.eastTexture = new int[]{x, y};
        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setSouthTexture(String texture) {
        return setSouthTexture(MOD_ID, texture);
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setSouthTexture(String modID, String texture) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.southTexture = TextureHelper.getOrCreateBlockTexture(modID, texture);
        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setSouthTexture(int x, int y) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.southTexture = new int[]{x, y};
        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setWestTexture(String texture) {
        return setWestTexture(MOD_ID, texture);
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setWestTexture(String modID, String texture) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.westTexture = TextureHelper.getOrCreateBlockTexture(modID, texture);
        return blockBuilder;
    }

    @SuppressWarnings({"unused"})
    public BlockBuilder setWestTexture(int x, int y) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.westTexture = new int[]{x, y};
        return blockBuilder;
    }
    @SuppressWarnings({"unused"})
    public <T extends Block> T build(T block) {
        if (topTexture != null) {
            block.atlasIndices[Side.TOP.getId()] = Block.texCoordToIndex(topTexture[0], topTexture[1]);
        }

        if (topTexture != null) {
            block.atlasIndices[Side.BOTTOM.getId()] = Block.texCoordToIndex(bottomTexture[0], bottomTexture[1]);
        }

        if (topTexture != null) {
            block.atlasIndices[Side.NORTH.getId()] = Block.texCoordToIndex(northTexture[0], northTexture[1]);
        }

        if (topTexture != null) {
            block.atlasIndices[Side.EAST.getId()] = Block.texCoordToIndex(eastTexture[0], eastTexture[1]);
        }

        if (topTexture != null) {
            block.atlasIndices[Side.SOUTH.getId()] = Block.texCoordToIndex(southTexture[0], southTexture[1]);
        }

        if (topTexture != null) {
            block.atlasIndices[Side.WEST.getId()] = Block.texCoordToIndex(westTexture[0], westTexture[1]);
        }

        if (hardness != null) {
            block.withHardness(hardness);
        }

        if (resistance != null) {
            block.withBlastResistance(resistance);
        }

        if (luminance != null) {
            block.withLightEmission(luminance);
        }

        if (lightOpacity != null) {
            block.withLightBlock(lightOpacity);
        }

        if (slipperiness != null) {
            block.movementScale = slipperiness;
        }

        block.withLitInteriorSurface(useInternalLight);

        if (immovable) {
            block.withImmovableFlagSet();
        }

        if (flammability != null) {
            BlockFire.setBurnRate(block.id, flammability[0], flammability[1]);
        }

        if (infiniburn) {
            block.withTags(BlockTags.INFINITE_BURN);
        }

        if (visualUpdateOnMetadata) {
            block.withDisabledNeighborNotifyOnMetadataChange();
        }

        if (tickOnLoad != null){
            block.setTicking(tickOnLoad);
        }

        if (blockDrop != null) {
            block.setDropOverride(blockDrop.id);
        }

        if (blockSound != null) {
            BlockSoundDispatcher.getInstance().addDispatch(block, blockSound);
        }

        if (blockColor != null) {
            BlockColorDispatcher.getInstance().addDispatch(block, blockColor);
        }

        if (blockModel != null) {
            BlockModelDispatcher.getInstance().addDispatch(block, blockModel);
        }

        if (customItemBlock != null) {
            Item.itemsList[block.id] = customItemBlock.run(block);
        } else {
            Item.itemsList[block.id] = new ItemBlock(block);
        }

        if (tags != null) {
            block.withTags(tags);
        }

        List<String> tokens = Arrays.stream(block.getKey().split("\\."))
                .filter(token -> !token.equals(MOD_ID))
                .collect(Collectors.toList());

        List<String> newTokens = new ArrayList<>();
        newTokens.add(MOD_ID);
        newTokens.addAll(tokens.subList(1, tokens.size()));

        block.setKey(StringUtils.join(newTokens, '.'));

        return block;
    }
    
    @FunctionalInterface
    public interface BlockLambda<T> {
        T run(Block block);
    }
}
