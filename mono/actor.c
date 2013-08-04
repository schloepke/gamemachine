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

void
on_receive (MonoObject *obj, unsigned char *bytes, int length) {
	MonoClass *klass;
  MonoDomain *domain;
	MonoMethod *onReceive = NULL, *m = NULL;
  MonoArray *array;
	void* iter;
	void* args [2];
fprintf (stderr, "on_receive message %s\n",bytes);

	klass = mono_object_get_class (obj);
	domain = mono_object_get_domain (obj);
  mono_thread_attach(domain);

	iter = NULL;
	while ((m = mono_class_get_methods (klass, &iter))) {
		if (strcmp (mono_method_get_name (m), "onReceive") == 0) {
			onReceive = m;
		}
  }
	//mono_runtime_invoke (onReceive, obj, NULL, NULL);


	array = mono_array_new (domain, mono_get_byte_class (), length);
  int i;
for(i = 0; i < length; i++)
    mono_array_set(array,char,i,bytes[i]);

  args[0] = array;
	mono_runtime_invoke (onReceive, obj, args, NULL);
}

MonoObject *create_object(MonoImage *image) {
  mono_thread_attach(mono_domain_get());
	MonoClass *klass;
	MonoObject *obj;

	klass = mono_class_from_name (image, "GameMachine", "Actor");
	if (!klass) {
		fprintf (stderr, "Can't find Actor in assembly %s\n", mono_image_get_filename (image));
		exit (1);
	}

	obj = mono_object_new (mono_domain_get(), klass);
	mono_runtime_object_init (obj);
fprintf (stderr, "object created 1\n");
  return obj;
}

int test_mono() {
return 1;
}

MonoImage *load_assembly(const char *file) {
  mono_thread_attach(mono_domain_get());
	MonoAssembly *assembly;
  MonoImage *image;
	assembly = mono_domain_assembly_open (mono_domain_get(), file);
	if (!assembly)
		return NULL;
	image = mono_assembly_get_image(assembly);
  return image;
}


void load_mono(const char *file) {
	mono_jit_init (file);
fprintf (stderr, "load_mono 1\n");
}

void unload_mono() {
	mono_environment_exitcode_get ();
	mono_jit_cleanup (mono_domain_get());
fprintf (stderr, "cleanup finished\n");
}

int 
main (int argc, char* argv[]) {
	const char *file;
	int retval = 1;
	
	if (argc < 2){
		fprintf (stderr, "Please provide an assembly to load\n");
		return 1;
	}
	file = argv [1];
	mono_jit_init (file);
  load_mono(file);
	return retval;
}

