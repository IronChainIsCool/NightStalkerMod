
package com.ironchainmodding.nightstalker;

import com.ironchainmodding.nightstalker.block.FastFallingBlock;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(NightStalkerMod.MODID)
public class NightStalkerMod {
    public static final String MODID = "nightstalker";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<FastFallingBlock> TUNGSTEN_CUBE_BLOCK = BLOCKS.register(
            "tungsten_cube",
            () -> new FastFallingBlock(
                    BlockBehaviour.Properties.of()
                            .destroyTime(6.0f)
                            .explosionResistance(100.0f)
            ));
    public static final DeferredItem<BlockItem> TUNGSTEN_CUBE_ITEM = ITEMS.registerSimpleBlockItem("tungsten_cube", TUNGSTEN_CUBE_BLOCK);

    public static final DeferredItem<SwordItem> STICK_OF_DOOM = ITEMS.registerItem(
            "stick_of_doom",
            props -> new SwordItem(
                    Tiers.WOOD,
                    props.attributes(SwordItem.createAttributes(Tiers.GOLD, 10000, -2.4F))
            ));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NIGHTSTALKER_TAB = CREATIVE_MODE_TABS.register("nightstalker_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.nightstalker"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> TUNGSTEN_CUBE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(TUNGSTEN_CUBE_ITEM.get());
                output.accept(STICK_OF_DOOM.get());
            }).build());

    public NightStalkerMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(TUNGSTEN_CUBE_ITEM);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}