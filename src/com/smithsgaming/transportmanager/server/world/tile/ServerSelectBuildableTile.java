package com.smithsgaming.transportmanager.server.world.tile;

import com.smithsgaming.transportmanager.common.player.*;
import com.smithsgaming.transportmanager.common.world.*;
import com.smithsgaming.transportmanager.util.common.*;

import java.util.*;

/**
 * @Author Marc (Created on: 05.05.2016)
 */
public class ServerSelectBuildableTile extends ServerBaseTile {
    ArrayList<String> replacementTiles;

    public ServerSelectBuildableTile (String identity, ArrayList<String> replacementTiles) {
        super(identity);
        this.replacementTiles = replacementTiles;
    }

    @Override
    public boolean isOverbuildableBy (IPlayer player, ITile newTile, WorldCoordinate worldCoordinate) {
        return replacementTiles.contains(newTile.getIdentity());
    }
}