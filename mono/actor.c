#include <mono/jit/jit.h>
#include <mono/metadata/object.h>
#include <mono/metadata/environment.h>
#include <mono/metadata/assembly.h>
#include <mono/metadata/debug-helpers.h>
#include <string.h>
#include <stdlib.h>

#ifndef FALSE
#define FALSE 0
#endif

int 
main (int argc, char* argv[]) {
	MonoDomain *domain;
	const char *file;
	int retval;
	
	if (argc < 2){
		fprintf (stderr, "Please provide an assembly to load\n");
		return 1;
	}
	file = argv [1];
  file = "invoke.exe";
	/*
	 * mono_jit_init() creates a domain: each assembly is
	 * loaded and run in a MonoDomain.
	 */
    fprintf (stderr, "FILE %s\n",file);
    fprintf (stderr, "argc %u\n",argc - 1);

	domain = mono_jit_init (file);

	create_object (domain, file);

	retval = mono_environment_exitcode_get ();
	
	mono_jit_cleanup (domain);
	return retval;
}

static void create_object(MonoDomain *domain, const char *file) {
	MonoAssembly *assembly;
	MonoClass *klass;
	MonoObject *obj;
  MonoImage *image;

	assembly = mono_domain_assembly_open (domain, file);
	if (!assembly)
		exit (2);

	image = mono_assembly_get_image(assembly);

	klass = mono_class_from_name (image, "GameMachine", "Actor");
	if (!klass) {
		fprintf (stderr, "Can't find Actor in assembly %s\n", mono_image_get_filename (image));
		exit (1);
	}

	obj = mono_object_new (domain, klass);
	mono_runtime_object_init (obj);
}
