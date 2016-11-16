package com.smithsgaming.transportmanager.client.render.world.chunk;

import com.smithsgaming.transportmanager.client.registries.GeometryRegistry;
import com.smithsgaming.transportmanager.client.render.*;
import com.smithsgaming.transportmanager.client.render.textures.Texture;
import com.smithsgaming.transportmanager.client.world.chunk.*;
import com.smithsgaming.transportmanager.main.core.*;
import com.smithsgaming.transportmanager.main.world.tiles.*;
import com.smithsgaming.transportmanager.util.*;

import java.util.*;

/**
 * @Author Marc (Created on: 04.04.2016)
 */
public class ChunkRenderer implements IRenderer {

    private ChunkClient chunkClient;
    private ChunkTileGeometry chunkGeometry;

    public ChunkRenderer (ChunkClient chunkClient) {
        this.chunkClient = chunkClient;

        onRebuild();
    }

    public void onRebuild () {
        onDestroyed();

        chunkGeometry = new ChunkTileGeometry(chunkClient);

        OpenGLUtil.loadGeometryIntoGPU(chunkGeometry);
    }

    public void onDestroyed () {
        if (chunkGeometry != null) OpenGLUtil.deleteGeometry(chunkGeometry);
    }

    @Override
    public void render () {
        chunkGeometry.render();
    }
}
