
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

int on_receive2 (MonoDomain *domain, MonoImage *image, const char *name_space, const char *name, const char *actor_id, const char *bytes, int length) {
  MonoClass *klass;
  MonoMethod *OnReceive = NULL;
  MonoObject *exception = NULL;
  MonoString *name_space_str, *klass_str, *actor_id_str;
  void* args [4];
  const char *actor_klass = "Actor";
  //mono_domain_set(domain,0);
  klass = mono_class_from_name (image, name_space, actor_klass);
  if (!klass) {
    fprintf (stderr, "Can't find %s %s in assembly %s\n", name_space, actor_klass, mono_image_get_filename (image));
    return 0;
  }
  OnReceive = mono_class_get_method_from_name(klass,"ReceiveMessage",4);

  if (OnReceive == 0) {
      fprintf (stderr, "NULL mono method\n");
    return 0;
  }

  MonoString *bytes2 = mono_string_new(domain,bytes);
  name_space_str = mono_string_new(domain,name_space);
  klass_str = mono_string_new(domain,name);
  actor_id_str = mono_string_new(domain,actor_id);
  args[0] = actor_id_str;
  args[1] = name_space_str;
  args[2] = klass_str;
  args[3] = bytes2;

  mono_runtime_invoke (OnReceive, NULL, args, &exception);
  if (exception) {
    MonoClass *error_klass = mono_object_get_class (exception);
    fprintf (stderr, "ERROR class %s\n", mono_class_get_name (error_klass));
    return 0;
  } else {
    return 1;
  }
}

//int on_receive (guint32 handle, const unsigned char *bytes, int length) {
int on_receive (MonoObject *obj, const unsigned char *bytes, int length) {
  MonoClass *klass;
  MonoDomain *domain;
  MonoMethod *OnReceive = NULL, *m = NULL;
  MonoProperty *prop;
  MonoArray *array;
  MonoObject *exception = NULL;
  void* iter;
  void* args [2];

  //gint32 domain_id;
  domain = mono_domain_get();
  //domain_id = mono_domain_get_id(mono_get_root_domain());
  //fprintf (stderr, "domain id %d\n", domain_id );
  //domain_id = mono_domain_get_id(domain);
  //fprintf (stderr, "domain id %d\n", domain_id );
  //mono_thread_attach(domain);

  //MonoObject* obj = mono_gchandle_get_target (handle);
  if (obj == NULL) {
      fprintf (stderr, "NULL mono obj\n");
    return 0;
  }
  klass = mono_object_get_class (obj);

  OnReceive = mono_class_get_method_from_name(klass,"OnReceive",1);

  if (OnReceive == 0) {
      fprintf (stderr, "NULL mono method\n");
    return 0;
  }


  array = mono_array_new (domain, mono_get_byte_class (), length);
  int i;
  for(i = 0; i < length; i++)
      mono_array_set(array,unsigned char,i,bytes[i]);

  args[0] = array;
  //mono_runtime_invoke (OnReceive, obj, args, NULL);
  //MonoObject* newobj = mono_object_new (mono_domain_get(), klass);
  //mono_runtime_object_init (obj);

  mono_runtime_invoke (OnReceive, obj, args, &exception);
  if (exception) {

    // leaving here for reference.  Need to figure out how to get full message
    // and backtrace from exceptions. 
    //MonoClass *error_klass = mono_class_from_name (image, name_space, name);
    
    MonoClass *error_klass = mono_object_get_class (exception);
    fprintf (stderr, "ERROR class %s\n", mono_class_get_name (error_klass));
    MonoMethod *ToString = mono_class_get_method_from_name(error_klass,"ToString",-1);
    iter = NULL ;
    while ((prop = mono_class_get_properties (error_klass, &iter))) {
      MonoMethod *pmethod = mono_property_get_get_method (prop);
      fprintf (stderr, "METHOD %s\n", mono_method_get_name(pmethod));
      //MonoObject *presult = mono_runtime_invoke (pmethod, exception, NULL, NULL);
    }
    iter = NULL ;
    while ((m = mono_class_get_methods (error_klass, &iter))) {
      fprintf (stderr, "METHOD %s\n", mono_method_get_name(m));
    }
    if (ToString) {
      mono_runtime_invoke(ToString,exception,NULL,NULL);
    } else {
      fprintf (stderr, "ToString not found\n");
    }
    
    return 0;
  } else {
    return 1;
  }
}

void attach_current_thread(MonoDomain *domain) {
  mono_thread_attach(domain);
}

void destroy_object(guint32 handle) {
  mono_gchandle_free(handle);
}

MonoObject* create_object(MonoImage *image, const char *name_space, const char *name) {
  MonoClass *klass;
  MonoObject *obj;

  klass = mono_class_from_name (image, name_space, name);
  if (!klass) {
    fprintf (stderr, "Can't find %s %s in assembly %s\n", name_space, name, mono_image_get_filename (image));
    return 0;
  }

  //fprintf (stderr, "loaded class %s\n", mono_class_get_name (klass));
  obj = mono_object_new (mono_domain_get(), klass);
  mono_runtime_object_init (obj);
  return obj;
}

guint32 icreate_object(MonoImage *image, const char *name_space, const char *name) {
  mono_thread_attach(mono_domain_get());
  MonoClass *klass;
  MonoObject *obj;

  klass = mono_class_from_name (image, name_space, name);
  if (!klass) {
    fprintf (stderr, "Can't find %s %s in assembly %s\n", name_space, name, mono_image_get_filename (image));
    return 0;
  }

  //fprintf (stderr, "loaded class %s\n", mono_class_get_name (klass));
  obj = mono_object_new (mono_domain_get(), klass);
  mono_runtime_object_init (obj);
  guint32 handle = mono_gchandle_new (obj, TRUE);
  return handle;
}


MonoImage *load_assembly(MonoDomain *domain, const char *file) {
  MonoAssembly *assembly;
  MonoImage *image;
  assembly = mono_domain_assembly_open (domain, file);
  if (!assembly)
    return NULL;

  image = mono_assembly_get_image(assembly);
  return image;
}

MonoDomain *create_domain(char *config) {
  MonoDomain *domain = mono_domain_create_appdomain("gamemachine",config);
  mono_domain_set(domain,0);
  return domain;
}

void load_mono(const char *file) {
  setenv ("MONO_DEBUG", "explicit-null-checks",1);
  mono_set_dirs ("/home2/chris/mono_local/lib", "/home2/chris/mono_local/etc");
  mono_config_parse (NULL);
  mono_jit_init (file);
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

