package turniplabs.halplibe.helper;

import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.item.model.ItemModel;

import net.minecraft.client.render.item.model.ItemModelBlock;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFire;
import net.minecraft.core.block.BlockLogicSupplier;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.sound.BlockSound;
import net.minecraft.core.util.helper.Side;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.mixin.accessors.BlocksAccessor;
import turniplabs.halplibe.util.registry.IdSupplier;
import turniplabs.halplibe.util.registry.RunLengthConfig;
import turniplabs.halplibe.util.registry.RunReserves;
import turniplabs.halplibe.util.toml.Toml;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public final class BlockBuilder implements Cloneable {

    private final @NotNull String modId;
    private final @NotNull String key;
    private final @NotNull String namespaceId;
    private final int id;
    private @Nullable Float hardness = null;
    private @Nullable Float resistance = null;
    private @Nullable Integer luminance = null;
    private @Nullable Integer lightOpacity = null;
    private @Nullable Float slipperiness = null;
    private boolean immovable = false;
    private boolean useInternalLight = false;
    private boolean visualUpdateOnMetadata = false;
    private @Nullable Boolean tickOnLoad = null;
    private boolean infiniburn = false;
    private int @Nullable [] flammability = null;
    private @Nullable BlockSound blockSound = null;
    private @Nullable Function<Block<?>, BlockColor> blockColor = null;
    private @NotNull Function<Block<?>, BlockModel<?>> blockModelSupplier = BlockModelStandard::new;
    private @NotNull Function<ItemBlock<?>, ItemModel> customItemModelSupplier = ItemModelBlock::new;
    private @Nullable BlockLambda<ItemBlock<?>> customBlockItem = null;
    private @Nullable Tag<Block<?>>[] tags = null;
    private String @NotNull [] textures = new String[6];
    private @Nullable String itemIcon = null;
//    private @Nullable

    public BlockBuilder(@NotNull String modId, @NotNull String name, int id) {
        this.modId = modId;
        this.key = String.format("tile.%s.%s", modId, name.replace("_", "."));
        this.namespaceId = String.format("%s:block/%s", modId, name);
        this.id = id;
    }

    public BlockBuilder(@NotNull String modId, @NotNull String translationKey, @NotNull String namespacedId, int id) {
        this.modId = modId;
        this.key = translationKey;
        this.namespaceId = namespacedId;
        this.id = id;
    }

    @Override
    public BlockBuilder clone() {
        try {
            // none of the fields are mutated so this should be fine
            BlockBuilder builder = (BlockBuilder) super.clone();
            builder.textures = new String[6];
            System.arraycopy(textures, 0, builder.textures, 0, textures.length);
            return builder;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Sets the texture on the side of the block model. Only applies if assigned block model extends {@link BlockModelStandard}
     * @param texture Texture key
     * @return Copy of the {@link BlockBuilder}
     */
    @SuppressWarnings("unused")
    public BlockBuilder setTopTexture(String texture){
        BlockBuilder builder = clone();
        builder.textures[Side.TOP.getId()] = texture;
        return builder;
    }

    /**
     * Sets the texture on the side of the block model. Only applies if assigned block model extends {@link BlockModelStandard}
     * @param texture Texture key
     * @return Copy of the {@link BlockBuilder}
     */
    @SuppressWarnings("unused")
    public BlockBuilder setBottomTexture(String texture){
        BlockBuilder builder = clone();
        builder.textures[Side.BOTTOM.getId()] = texture;
        return builder;
    }

    /**
     * Sets the texture on the side of the block model. Only applies if assigned block model extends {@link BlockModelStandard}
     * @param texture Texture key
     * @return Copy of the {@link BlockBuilder}
     */
    @SuppressWarnings("unused")
    public BlockBuilder setNorthTexture(String texture){
        BlockBuilder builder = clone();
        builder.textures[Side.NORTH.getId()] = texture;
        return builder;
    }

    /**
     * Sets the texture on the side of the block model. Only applies if assigned block model extends {@link BlockModelStandard}
     * @param texture Texture key
     * @return Copy of the {@link BlockBuilder}
     */
    @SuppressWarnings("unused")
    public BlockBuilder setSouthTexture(String texture){
        BlockBuilder builder = clone();
        builder.textures[Side.SOUTH.getId()] = texture;
        return builder;
    }

    /**
     * Sets the texture on the side of the block model. Only applies if assigned block model extends {@link BlockModelStandard}
     * @param texture Texture key
     * @return Copy of the {@link BlockBuilder}
     */
    @SuppressWarnings("unused")
    public BlockBuilder setWestTexture(String texture){
        BlockBuilder builder = clone();
        builder.textures[Side.WEST.getId()] = texture;
        return builder;
    }

    /**
     * Sets the texture on the side of the block model. Only applies if assigned block model extends {@link BlockModelStandard}
     * @param texture Texture key
     * @return Copy of the {@link BlockBuilder}
     */
    @SuppressWarnings("unused")
    public BlockBuilder setEastTexture(String texture){
        BlockBuilder builder = clone();
        builder.textures[Side.EAST.getId()] = texture;
        return builder;
    }

    /**
     * Sets the texture on the side of the block model. Only applies if assigned block model extends {@link BlockModelStandard}
     * @param texture Texture key
     * @return Copy of the {@link BlockBuilder}
     */
    @SuppressWarnings("unused")
    public BlockBuilder setTextures(String texture){
        BlockBuilder builder = clone();
        builder.textures[Side.TOP.getId()] = texture;
        builder.textures[Side.BOTTOM.getId()] = texture;
        builder.textures[Side.NORTH.getId()] = texture;
        builder.textures[Side.SOUTH.getId()] = texture;
        builder.textures[Side.WEST.getId()] = texture;
        builder.textures[Side.EAST.getId()] = texture;
        return builder;
    }

    /**
     * Sets the texture on the side of the block model. Only applies if assigned block model extends {@link BlockModelStandard}
     * @param texture Texture key
     * @return Copy of the {@link BlockBuilder}
     */
    @SuppressWarnings("unused")
    public BlockBuilder setSideTextures(String texture){
        BlockBuilder builder = clone();
        builder.textures[Side.NORTH.getId()] = texture;
        builder.textures[Side.SOUTH.getId()] = texture;
        builder.textures[Side.WEST.getId()] = texture;
        builder.textures[Side.EAST.getId()] = texture;
        return builder;
    }

    /**
     * Sets the texture on the side of the block model. Only applies if assigned block model extends {@link BlockModelStandard}
     * @param texture Texture key
     * @return Copy of the {@link BlockBuilder}
     */
    @SuppressWarnings("unused")
    public BlockBuilder setTopBottomTextures(String texture){
        BlockBuilder builder = clone();
        builder.textures[Side.TOP.getId()] = texture;
        builder.textures[Side.BOTTOM.getId()] = texture;
        return builder;
    }

    /**
     * Sets the texture on the side of the block model. Only applies if assigned block model extends {@link BlockModelStandard}
     * @param texture Texture key
     * @return Copy of the {@link BlockBuilder}
     */
    @SuppressWarnings("unused")
    public BlockBuilder setNorthSouthTextures(String texture){
        BlockBuilder builder = clone();
        builder.textures[Side.NORTH.getId()] = texture;
        builder.textures[Side.SOUTH.getId()] = texture;
        return builder;
    }

    /**
     * Sets the texture on the side of the block model. Only applies if assigned block model extends {@link BlockModelStandard}
     * @param texture Texture key
     * @return Copy of the {@link BlockBuilder}
     */
    @SuppressWarnings("unused")
    public BlockBuilder setEastWestTextures(String texture){
        BlockBuilder builder = clone();
        builder.textures[Side.WEST.getId()] = texture;
        builder.textures[Side.EAST.getId()] = texture;
        return builder;
    }

    /**
     * Sets the icon for the {@link Item}'s {@link ItemModel}, only works if the ItemModel used extends {@link ItemModelStandard} and the {@link BlockModel} renders as 2D
     * @param iconKey texture key for the icon for the item to use. Example "minecraft:item/stick"
     * @return @return Copy of {@link ItemBuilder}
     */
    @SuppressWarnings("unused")
    public BlockBuilder setIcon(String iconKey){
        BlockBuilder builder = clone();
        builder.itemIcon = iconKey;
        return builder;
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
    public BlockBuilder setBlockColor(Function<Block<?>, BlockColor> blockColorSupplier) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.blockColor = blockColorSupplier;
        return blockBuilder;
    }

    /**
     * Sets the block's visible model.<br>
     * Example code:
     * <pre>{@code
     *     public static final Block customFlower = new BlockBuilder(MOD_ID)
     *          .setBlockModel(block -> new BlockModelCrossedSquares<>(block)))
     *          .build(new BlockFlower("custom.flower", 4002);
     * }</pre>
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setBlockModel(@NotNull Function<Block<?>, BlockModel<?>> blockModelSupplier) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.blockModelSupplier = blockModelSupplier;
        return blockBuilder;
    }

    /**
     * Sets the item form of the block's visible model.<br>
     * Example code:
     * <pre>{@code
     *     public static final Block customFlower = new BlockBuilder(MOD_ID)
     *          .setItemModel(block -> new ItemModelBlock(block, MOD_ID)))
     *          .build(new BlockFlower("custom.flower", 4002);
     * }</pre>
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setItemModel(@NotNull Function<ItemBlock<?>, ItemModel> itemModelSupplier) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.customItemModelSupplier = itemModelSupplier;
        return blockBuilder;
    }

    /**
     * Sets the block's item used to place the block.<br>
     * Example code:
     * <pre>{@code
     *     public static final Block customSlab = new BlockBuilder(MOD_ID)
     *          .setBlockItem(BlockItemSlab::new)
     *          .build(new BlockSlab(Block.dirt, 4003));
     * }</pre>
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setBlockItem(BlockLambda<ItemBlock<?>> customBlockItem) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.customBlockItem = customBlockItem;
        return blockBuilder;
    }

    /**
     * Overrides all previous tags with the ones provided
     */
    @SafeVarargs
    @SuppressWarnings({"unused"})
    public final BlockBuilder setTags(Tag<Block<?>>... tags) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.tags = tags;
        return blockBuilder;
    }

    /**
     * Adds provided tags to previously specified tags
     */
    @SafeVarargs
    @SuppressWarnings({"unused"})
    public final BlockBuilder addTags(Tag<Block<?>>... tags) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.tags = ArrayUtils.addAll(this.tags, tags);
        return blockBuilder;
    }

    /**
     * Applies the builder configuration to the supplied block.
     * @param blockLogicSupplier Input block object
     * @return Returns the input block after builder settings are applied to it.
     */
    @SuppressWarnings({"unused"})
    public <T extends BlockLogic> Block<T> build(BlockLogicSupplier<T> blockLogicSupplier) {
        // TODO add tile entity supplier build arg
        Block<T> block = Blocks.register(key, namespaceId, id, blockLogicSupplier);
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
            block.friction = slipperiness;
        }

        block.withLitInteriorSurface(useInternalLight);

        if (immovable) {
            block.withImmovableFlagSet();
        }

        if (flammability != null) {
            BlockLogicFire.setFlammable(block, flammability[0], flammability[1]);
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

        if (blockSound != null) {
            block.withSound(blockSound);
        }

        Assignment.queueBlockColor(block, blockColor);

        ItemBlock<?> BlockItem;

        if (tags != null) {
            block.withTags(tags);
        }

        if (BlocksAccessor.hasInit()) {
            block.init();

            if (customBlockItem != null) {
                Item.itemsList[block.id()] = BlockItem = customBlockItem.run(block);
            } else {
                Item.itemsList[block.id()] = BlockItem = new ItemBlock<>(block);
            }

            block.getLogic().initializeBlock();
            BlocksAccessor.cacheBlock(block);
        }

        Assignment.queueBlockModel(block, blockModelSupplier, textures);
//        ItemBuilder.Assignment.queueItemModel(BlockItem, customItemModelSupplier, itemIcon); // TODO reimpl item model

        return block;
    }
    
    @FunctionalInterface
    public interface BlockLambda<T> {
        T run(Block<?> block);
    }
    public static class Registry{
        public static int highestVanilla;

        private static final RunReserves reserves = new RunReserves(
                Registry::findOpenIds,
                Registry::findLength
        );

        /**
         * Should be called in a runnable scheduled with {@link IdSupplierHelper#scheduleRegistry(boolean, Runnable)}
         * @param count the amount of needed blocks for the mod
         * @return the first available slot to register in
         */
        public static int findOpenIds(int count) {
            int run = 0;
            for (int i = highestVanilla; i < Blocks.blocksList.length; i++) {
                if (Blocks.blocksList[i] == null && !reserves.isReserved(i)) {
                    if (run >= count)
                        return (i - run);
                    run++;
                } else {
                    run = 0;
                }
            }
            return -1;
        }

        public static int findLength(int id, int terminate) {
            int run = 0;
            for (int i = id; i < Blocks.blocksList.length; i++) {
                if (Blocks.blocksList[i] == null && !reserves.isReserved(i)) {
                    run++;
                    if (run >= terminate) return terminate;
                } else {
                    return run;
                }
            }
            return run;
        }

        /**
         * Allows halplibe to automatically figure out where to insert the runs
         * @param modId     an identifier for the mod, can be anything, but should be something the user can identify
         * @param runs      a toml object representing configured registry runs
         * @param neededIds the number of needed ids
         *                  if this changes after the mod has been configured (i.e. mod updated and now has more blocks) it'll find new, valid runs to put those blocks into
         * @param function  the function to run for registering items
         */
        public static void reserveRuns(String modId, Toml runs, int neededIds, Consumer<IdSupplier> function) {
            RunLengthConfig cfg = new RunLengthConfig(runs, neededIds);
            cfg.register(reserves);
            IdSupplierHelper.scheduleSmartRegistry(
                    () -> {
                        IdSupplier supplier = new IdSupplier(modId, reserves, cfg, neededIds);
                        function.accept(supplier);
                        supplier.validate();
                    }
            );
        }
    }
    public static class Assignment{
        public static boolean blockDispatcherInitialized = false;
        public static final List<BlockAssignmentEntry<?>> queuedBlockModels = new ArrayList<>();

        /**
         *  Queues a BlockModel assignment until the game is ready to do so
         */
        public static <T extends Block> void queueBlockModel(@NotNull T block, Function<T, BlockModel<?>> blockModelSupplier, @Nullable String[] textures){
            if (!HalpLibe.isClient) return;
            if (blockModelSupplier == null) return;

            if (blockDispatcherInitialized){
                BlockModelDispatcher.getInstance().addDispatch(new BlockAssignmentEntry<>(block, blockModelSupplier, textures).getModel());
                return;
            }
            queuedBlockModels.add(new BlockAssignmentEntry<>(block, blockModelSupplier, textures));
        }

        public static class BlockAssignmentEntry<T extends Block>{
            public final T block;
            public final Function<T, BlockModel<?>> modelFunction;
            public final String[] textures;

            public BlockAssignmentEntry(@NotNull T block, @NotNull Function<T, BlockModel<?>> modelFunction, @Nullable String[] textures){
                this.block = block;
                this.modelFunction = modelFunction;
                this.textures = textures;
            }
            public BlockModel<?> getModel(){
                BlockModel<?> model = modelFunction.apply(block);

                if (model instanceof BlockModelStandard && textures != null){
                    for (int i = 0; i < textures.length; i++) {
                        if (textures[i] != null){
                            ((BlockModelStandard<?>) model).setTex(BlockModelStandard.BLOCK_TEXTURES, textures[i], Side.getSideById(i));
                        }
                    }
                }
                return model;
            }
        }
        public static boolean blockColorDispatcherInitialized = false;
        public static final Map<Block<?>, Function<Block<?>, BlockColor>> queuedBlockColors = new LinkedHashMap<>();
        /**
         *  Queues a BlockColor assignment until the game is ready to do so
         */
        public static void queueBlockColor(@NotNull Block<?> block, Function<Block<?>, BlockColor> blockColorSupplier){
            if (!HalpLibe.isClient) return;
            if (blockColorSupplier == null) return;

            if (blockColorDispatcherInitialized){
                BlockColorDispatcher.getInstance().addDispatch(block, blockColorSupplier.apply(block));
                return;
            }
            queuedBlockColors.put(block, blockColorSupplier);
        }
    }
}
