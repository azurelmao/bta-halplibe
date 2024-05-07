package turniplabs.halplibe.helper;

import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelStairs;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelBlock;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFire;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.sound.BlockSound;
import net.minecraft.core.sound.BlockSoundDispatcher;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.util.registry.IdSupplier;
import turniplabs.halplibe.util.registry.RunLengthConfig;
import turniplabs.halplibe.util.registry.RunReserves;
import turniplabs.halplibe.util.toml.Toml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class BlockBuilder implements Cloneable {

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
    private Function<Block, BlockColor> blockColor = null;
    @NotNull
    private Function<Block, BlockModel<?>> blockModelSupplier = BlockModelStairs::new;
    @NotNull
    private Function<ItemBlock, ItemModel> customItemModelSupplier = ItemModelBlock::new;
    private BlockLambda<ItemBlock> customItemBlock = null;
    private Tag<Block>[] tags = null;

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
    public BlockBuilder setBlockColor(Function<Block, BlockColor> blockColorSupplier) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.blockColor = blockColorSupplier;
        return blockBuilder;
    }

    /**
     * Sets the block's visible model.<br>
     * Example code:
     * <pre>{@code
     *     public static final Block customFlower = new BlockBuilder(MOD_ID)
     *          .setBlockModel(BlockModelCrossedSquares::new))
     *          .build(new BlockFlower("custom.flower", 4002);
     * }</pre>
     */
    @SuppressWarnings({"unused"})
    public BlockBuilder setBlockModel(@NotNull Function<Block, BlockModel<?>> blockModelSupplier) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.blockModelSupplier = blockModelSupplier;
        return blockBuilder;
    }
    public BlockBuilder setItemModel(@NotNull Function<ItemBlock, ItemModel> itemModelSupplier) {
        BlockBuilder blockBuilder = this.clone();
        blockBuilder.customItemModelSupplier = itemModelSupplier;
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
    public <T extends Block> T build(T block) {
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

        Assignment.queueBlockColor(block, blockColor);

        ItemBlock itemBlock;

        if (customItemBlock != null) {
            Item.itemsList[block.id] = itemBlock = customItemBlock.run(block);
        } else {
            Item.itemsList[block.id] = itemBlock = new ItemBlock(block);
        }

        if (tags != null) {
            block.withTags(tags);
        }

        Assignment.queueBlockModel(block, blockModelSupplier);
        ItemBuilder.Assignment.queueItemModel(itemBlock, customItemModelSupplier);

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
    public static class Registry{
        public static int highestVanilla;

        private static final RunReserves reserves = new RunReserves(
                Registry::findOpenIds,
                Registry::findLength
        );

        /**
         * Should be called in a runnable scheduled with {@link RegistryHelper#scheduleRegistry(boolean, Runnable)}
         * @param count the amount of needed blocks for the mod
         * @return the first available slot to register in
         */
        public static int findOpenIds(int count) {
            int run = 0;
            for (int i = highestVanilla; i < Block.blocksList.length; i++) {
                if (Block.blocksList[i] == null && !reserves.isReserved(i)) {
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
            for (int i = id; i < Block.blocksList.length; i++) {
                if (Block.blocksList[i] == null && !reserves.isReserved(i)) {
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
            RegistryHelper.scheduleSmartRegistry(
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
        public static final Map<Block, Function<Block, BlockModel<?>>> queuedBlockModels = new LinkedHashMap<>();
        public static void queueBlockModel(@NotNull Block block, Function<Block, BlockModel<?>> blockModelSupplier){
            if (!HalpLibe.isClient) return;
            if (blockModelSupplier == null) return;

            if (blockDispatcherInitialized){
                BlockModelDispatcher.getInstance().addDispatch(blockModelSupplier.apply(block));
                return;
            }
            queuedBlockModels.put(block, blockModelSupplier);
        }
        public static boolean blockColorDispatcherInitialized = false;
        public static final Map<Block, Function<Block, BlockColor>> queuedBlockColors = new LinkedHashMap<>();
        public static void queueBlockColor(@NotNull Block block, Function<Block, BlockColor> blockColorSupplier){
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
