rm -rf build
cp overrides/DetourNavMeshQuery.cpp recastnavigation/Detour/Source
cp overrides/DetourNavMeshQuery.h recastnavigation/Detour/Include
cp overrides/DetourCrowd.cpp recastnavigation/DetourCrowd/Source
premake/premake4 gmake
cd build && make