package neresources.compatibility.bigreactors;

import erogenousbeef.bigreactors.world.BRSimpleOreGenerator;
import erogenousbeef.bigreactors.world.BRWorldGenerator;
import neresources.api.distributions.DistributionSquare;
import neresources.compatibility.CompatBase;
import neresources.registry.OreEntry;
import neresources.utils.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.util.Set;

public class BigReactorsCompat extends CompatBase {

    @Override
    protected void init() {
        Set<BRSimpleOreGenerator> oreGeneratorSet = (Set<BRSimpleOreGenerator>) ReflectionHelper.getObject(BRWorldGenerator.class,"oreGenerators",null);
        if (oreGeneratorSet==null) return;
        for (BRSimpleOreGenerator oreGen:oreGeneratorSet)
        {
            if (oreGen==null) continue;
            int minY = ReflectionHelper.getInt(BRSimpleOreGenerator.class,"minY",oreGen);
            int maxY = ReflectionHelper.getInt(BRSimpleOreGenerator.class,"maxY",oreGen);
            int minVeins = ReflectionHelper.getInt(BRSimpleOreGenerator.class,"minClustersPerChunk",oreGen);
            int maxVeins = ReflectionHelper.getInt(BRSimpleOreGenerator.class,"maxClustersPerChunk",oreGen);
            boolean isObf = ReflectionHelper.doesFieldExist(WorldGenMinable.class, "field_76541_b");
            System.out.println("Obfuscated Minecraft: "+isObf);
            int veinSize = 10;//ReflectionHelper.getInt(WorldGenMinable.class, isObf ? "field_76541_b" : "numberOfBlocks",oreGen).intValue();
            Block oreBlock = (Block) ReflectionHelper.getObject(BRSimpleOreGenerator.class,"blockToGenerate",oreGen);
            Block toReplace = (Block) ReflectionHelper.getObject(BRSimpleOreGenerator.class,"blockToReplace",oreGen);
            int metadata = ReflectionHelper.getInt(BRSimpleOreGenerator.class,"blockToGenerateMetadata",oreGen);
            Set<Integer> dimensionBlacklist = (Set<Integer>) ReflectionHelper.getObject(BRSimpleOreGenerator.class,"dimensionBlacklist",oreGen);
            ItemStack ore = new ItemStack(oreBlock,1,metadata);
            float chance = ((float)(minVeins+maxVeins)/2)*veinSize/((maxY-minY+1)*256);
            OreEntry entry = new OreEntry(ore,new DistributionSquare(Math.max(0, minY - veinSize / 2), minY, maxY, Math.min(maxY + veinSize / 2, 255), chance));
            registerOre(entry);
        }
    }
}
