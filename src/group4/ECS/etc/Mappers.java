package group4.ECS.etc;

import com.badlogic.ashley.core.ComponentMapper;
import group4.ECS.components.PositionComponent;

public class Mappers {

    public static final ComponentMapper<PositionComponent> positionMapper =
            ComponentMapper.getFor(PositionComponent.class);

}
