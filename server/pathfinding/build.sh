rm -rf build
cp overrides/DetourNavMeshQuery.cpp recastnavigation/Detour/Source
cp overrides/DetourNavMeshQuery.h recastnavigation/Detour/Include
premake/premake4 gmake
cd build && make