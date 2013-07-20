Movement is not smooth basically because movement occurs in the
simulation system and the simulation system is discrete. To smooth out
the movement things can be done with animation type effects. For
movement the user can have a direction and velocity, when rendered we
can take into account the time of the rendering.

Combining the texture together greatly increased the number of models
which can be rendered at a specific time.

Need to improve the rendering system to be able to do animations. This
involves using the shaders to apply animations to the faces.

There needs to be more preprocessing of the models to get them into a
format that is suitable for rendering. All textures need to be loaded
ahead of time.

The perspective currenly doesn't feel right. The movement scale is
off.

Should add tool selection using the tab key. This will allow more
tools to be added to the game, so for example one can have a tool that
modifies the viewport parameters.

Should add gravity to keep the character grounded. Should also modify
the ground pattern to be a single cube rather than very many small
cubes.

== Animations

Models are trees. The nodes have lists of translations and rotations
associated with them. A value alpha selects between two elements in
the lists and then interpolation is performed to find the actual
rotations and translation required.

We'd like this to occur on the GPU. For rendering using shaders there
are about 15 arrays that we can use. So for each point we'd like to
pass a single alpha value and then interp the position.

It might be easier to develop this code first in isolation and then
define a translation into the required format.

The question is then how to pass in the rotation and translation
matricies.





