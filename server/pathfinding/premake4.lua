local action = _ACTION or ""
local todir = "build/"

solution "pathfinding"
	configurations { 
		"debug",
		"release"
	}
	location (todir)

	-- extra warnings, no exceptions or rtti
	flags { 
		"ExtraWarnings",
		"NoExceptions",
		"NoRTTI"
	}

	-- debug configs
	configuration "debug*"
		defines { "DEBUG" }
		flags { "Symbols" }

		targetdir ( "build/" .. action .. "/debug" )
	-- linux library cflags and libs
	configuration { "linux", "gmake" }
		buildoptions { 
      "-fpic"
		}
		linkoptions { 
      "-fpic"
		}

	-- windows library cflags and libs
	configuration { "windows" }
 
 	-- release configs
	configuration "release*"
		defines { "NDEBUG" }
		flags { "Optimize" }
		targetdir ( "build/" .. action .. "/release" )

	-- windows specific
	configuration "windows"
		defines { "WIN32", "_WINDOWS", "_CRT_SECURE_NO_WARNINGS" }

  project "Detour"
	language "C++"
	kind "StaticLib"
	includedirs { 
		"recastnavigation/Detour/Include" 
	}
	files { 
		"recastnavigation/Detour/Include/*.h", 
		"recastnavigation/Detour/Source/*.cpp" 
	}
	targetdir (todir .. "/lib")

project "detour_path"
	language "C++"
	kind "SharedLib"
	includedirs { 
		"recastnavigation/Detour/Include",
		"recastnavigation/Recast/Include",
		"include"
	}

  libdirs {todir .. "lib" }
	files { 
		"recastnavigation/Detour/Include/*.h", 
		"recastnavigation/Recast/Include/Recast.h", 
		"recastnavigation/Detour/Source/*.cpp",
		"include/*.h",
		"*.cpp"
	}
	targetdir (todir .. "/lib")

	-- project dependencies
	links { 
		"Detour"
	}

project "pathfind_test"
	language "C++"
	kind "ConsoleApp"
	includedirs { 
		"recastnavigation/Detour/Include",
		"recastnavigation/Recast/Include", 
		"include"
	}
  libdirs {todir .. "lib" }
	files { 
		"recastnavigation/Detour/Include/*.h", 
		"recastnavigation/Recast/Include/Recast.h", 
		"recastnavigation/Detour/Source/*.cpp",
		"include/*.h",
		"*.cpp"
	}
	targetdir (todir .. "/bin")
	links { 
		"Detour"
	}
