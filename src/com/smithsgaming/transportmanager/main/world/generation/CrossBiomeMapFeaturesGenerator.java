package com.smithsgaming.transportmanager.main.world.generation;

import com.hoten.delaunay.voronoi.Center;
import com.hoten.delaunay.voronoi.Edge;
import com.smithsgaming.transportmanager.main.TransportManager;
import com.smithsgaming.transportmanager.main.core.BiomeManager;
import com.smithsgaming.transportmanager.main.world.biome.BaseBiome;
import com.smithsgaming.transportmanager.main.world.biome.Biome;
import com.smithsgaming.transportmanager.main.world.chunk.Chunk;
import com.smithsgaming.transportmanager.main.world.tiles.Tile;
import com.smithsgaming.transportmanager.util.concurrent.ProgressionNotifierThread;
import com.smithsgaming.transportmanager.util.math.Vector2i;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Vector;

/**
 * Created by Marc on 17-11-2016.
 */
public class CrossBiomeMapFeaturesGenerator implements IWorldGenFeature {

    public static final CrossBiomeMapFeaturesGenerator instance = new CrossBiomeMapFeaturesGenerator();

    private final int MAXTILEDISTANCE = 2;
    private final int MAXNOISE = 25;

    @Override
    public void generate(WorldGenerationData worldGenerationData, ProgressionNotifierThread runningThread) {
        int maxCount = worldGenerationData.getWorldWidth() * worldGenerationData.getWorldHeight();
        float progressionStep = 1F / maxCount;
        float totalProgression = 0F;

        runningThread.onThreadProgressionChanged(totalProgression, 1, "Started modifying Biomeborders...");
        for (int x = 0; x < worldGenerationData.getWorldWidth(); x++) {
            for (int y = 0; y < worldGenerationData.getWorldHeight(); y++) {
                BaseBiome biome = worldGenerationData.getBiomeAtPos(x, y);

                //Data Setup
                Vector2i point = new Vector2i(x, y);
                Center polygon = WorldGenUtil.getPolygonForPoint(worldGenerationData, point);
                if (polygon == null) continue;

                //Find the closest edges
                List<Edge> closestEdges = WorldGenUtil.getClosestEdgeToPoint(polygon, point, MAXTILEDISTANCE);
                if (closestEdges.isEmpty()) continue;

                //Not all border tiles should be modified
                if (worldGenerationData.getGenerationRandom().nextInt(MAXNOISE) > 0) continue;

                //Chose the edge to cross and retrieve the crossed polygon
                Edge crossEdge = closestEdges.get(worldGenerationData.getGenerationRandom().nextInt(closestEdges.size()));
                Center crossPolygon;
                if (crossEdge.d0 == polygon) {
                    crossPolygon = crossEdge.d1;
                } else if (crossEdge.d1 == polygon) {
                    crossPolygon = crossEdge.d0;
                } else {
                    //Borders do not have other crosspolygons, so skipping.
                    continue;
                }

                BaseBiome crossBiome = BiomeManager.instance.getBaseBiomeForGenerationColor(BiomeManager.instance.getBaseBiomeForCenter(crossPolygon).getGenerationColor());
                if (crossBiome == null) continue;
                if (crossBiome == biome) continue;

                //if (worldGenerationData.getGenerationRandom().nextInt(MAXNOISE) > 0) continue;

                Tile replacedTile = crossBiome.getTile();

                totalProgression += progressionStep;
                runningThread.onThreadProgressionChanged(totalProgression, 1, "Generated Biome: " + crossBiome.getBiomeType().getName() + " for X: " + x + " Y: " + y);
                worldGenerationData.world.setTileAtPos(replacedTile, x, y);
            }
        }
    }
}
