
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <glib.h>
#include <mono/jit/jit.h>
#include <mono/metadata/object.h>
#include <mono/metadata/environment.h>
#include <mono/metadata/assembly.h>
#include <mono/metadata/debug-helpers.h>
#ifndef FALSE
#define FALSE 0
#endif


int on_receive (guint32 handle, unsigned char *bytes, int length) {
  MonoClass *klass;
  MonoDomain *domain;
  MonoMethod *OnReceive = NULL, *m = NULL;
  MonoArray *array;
  void* iter;
  void* args [2];

  MonoObject* obj = mono_gchandle_get_target (handle);
  domain = mono_domain_get();
  mono_thread_attach(domain);
  klass = mono_object_get_class (obj);

  iter = NULL ;
  while ((m = mono_class_get_methods (klass, &iter))) {
    if (strcmp (mono_method_get_name (m), "OnReceive") == 0) {
      OnReceive = m;
    }
  }
  if (OnReceive == 0) {
    return 0;
  }


  array = mono_array_new (domain, mono_get_byte_class (), length);
  int i;
  for(i = 0; i < length; i++)
      mono_array_set(array,unsigned char,i,bytes[i]);

  args[0] = array;
  mono_runtime_invoke (OnReceive, obj, args, NULL);
  return 1;
}

void attach_current_thread() {
  mono_domain_get();
  mono_thread_attach(mono_get_root_domain());
}

void destroy_object(guint32 handle) {
  mono_gchandle_free(handle);
}

guint32 create_object(MonoImage *image) {
  mono_thread_attach(mono_domain_get());
  MonoClass *klass;
  MonoObject *obj;

  klass = mono_class_from_name (image, "GameMachine", "Actor");
  if (!klass) {
    fprintf (stderr, "Can't find Actor in assembly %s\n", mono_image_get_filename (image));
    exit (1);
  }

  //fprintf (stderr, "loaded class %s\n", mono_class_get_name (klass));
  obj = mono_object_new (mono_domain_get(), klass);
  mono_runtime_object_init (obj);
  guint32 handle = mono_gchandle_new (obj, TRUE);
  return handle;
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
  mono_thread_attach(mono_domain_get());
  fprintf (stderr, "load_mono 1\n");
}

void unload_mono() {
  mono_environment_exitcode_get ();
  mono_jit_cleanup (mono_domain_get());
  fprintf (stderr, "cleanup finished\n");
}

void send_message(unsigned char *message) {
  fprintf (stderr, "send_message called\n");
}

int main (int argc, char* argv[]) {
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

