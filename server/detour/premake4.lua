local action = _ACTION or ""
local todir = "build/"

solution "recastnavigation"
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
 
 	-- release configs
	configuration "release*"
		defines { "NDEBUG" }
		flags { "Optimize" }
		targetdir ( "build/" .. action .. "/release" )

	-- windows specific
	configuration "windows"
		defines { "WIN32", "_WINDOWS", "_CRT_SECURE_NO_WARNINGS" }

project "pathfind"
	language "C++"
	kind "SharedLib"
	includedirs { 
		"include"
	}
  libdirs {"lib" }
	files { 
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
		"include"
	}
  libdirs {"lib" }
	files { 
		"include/*.h",
		"*.cpp"
	}
	targetdir (todir .. "/bin")
	links { 
		"Detour"
	}

	-- linux library cflags and libs
	configuration { "linux", "gmake" }
		buildoptions { 
		}
		linkoptions { 
		}

	-- windows library cflags and libs
	configuration { "windows" }

