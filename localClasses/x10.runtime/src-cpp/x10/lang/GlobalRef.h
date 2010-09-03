#ifndef __X10_LANG_GLOBALREF
#define __X10_LANG_GLOBALREF

#include <x10rt.h>

#include <x10/util/GlobalRef.struct_h>

#endif // X10_LANG_GLOBALREF

namespace x10 { namespace util { 
template<class T> class GlobalRef;
} } 

#ifndef X10_LANG_GLOBALREF_NODEPS
#define X10_LANG_GLOBALREF_NODEPS
#include <x10/lang/Any.h>
#include <x10/lang/String.h>
#ifndef X10_LANG_GLOBALREF_GENERICS
#define X10_LANG_GLOBALREF_GENERICS
#endif // X10_LANG_GLOBALREF_GENERICS
#ifndef X10_LANG_GLOBALREF_IMPLEMENTATION
#define X10_LANG_GLOBALREF_IMPLEMENTATION
#include <x10/util/GlobalRef.h>


// ITable junk, both for GlobalRef and IBox<GlobalRef>
namespace x10 {
    namespace lang { 

        template<class T> class GlobalRef_ithunk0 : public x10::util::GlobalRef<T> {
        public:
            static x10::lang::Any::itable<GlobalRef_ithunk0<T> > itable;
        };

        template<class T> x10::lang::Any::itable<GlobalRef_ithunk0<T> >
            GlobalRef_ithunk0<T>::itable(&GlobalRef<T>::equals,
                                         &GlobalRef<T>::hashCode,
                                         &GlobalRef<T>::toString,
                                         &GlobalRef_ithunk0<T>::typeName);

        template<class T> class GlobalRef_iboxithunk0 : public x10::lang::IBox<x10::util::GlobalRef<T> > {
        public:
            static x10::lang::Any::itable<GlobalRef_iboxithunk0<T> > itable;
            x10_boolean equals(x10aux::ref<x10::lang::Any> arg0) {
                return this->value->equals(arg0);
            }
            x10_int hashCode() {
                return this->value->hashCode();
            }
            x10aux::ref<x10::lang::String> toString() {
                return this->value->toString();
            }
            x10aux::ref<x10::lang::String> typeName() {
                return this->value->typeName();
            }
        };

        template<class T> x10::lang::Any::itable<GlobalRef_iboxithunk0<T> >
            GlobalRef_iboxithunk0<T>::itable(&GlobalRef_iboxithunk0<T>::equals,
                                             &GlobalRef_iboxithunk0<T>::hashCode,
                                             &GlobalRef_iboxithunk0<T>::toString,
                                             &GlobalRef_iboxithunk0<T>::typeName);
    }
} 


template<class T> void x10::util::GlobalRef<T>::_serialize(x10::util::GlobalRef<T> this_,
                                                                    x10aux::serialization_buffer& buf) {
    buf.write((x10_long)(size_t)(this_->location));
    buf.write((x10_long)(size_t)(this_->ref.operator->()));
}

template<class T> void x10::util::GlobalRef<T>::_deserialize_body(x10aux::deserialization_buffer& buf) {
    location = buf.read<x10aux::place>();
    ref = (T*)(size_t)buf.read<x10_long>();
}


template<class T> x10_boolean x10::util::GlobalRef<T>::_struct_equals(x10aux::ref<x10::lang::Any> that) {
    if ((!(x10aux::instanceof<x10::util::GlobalRef<T> >(that)))) {
        return false;
    }
    return _struct_equals(x10aux::class_cast<x10::util::GlobalRef<T> >(that));
}

template<class T> x10aux::ref<x10::lang::String> x10::util::GlobalRef<T>::toString() {
    char* tmp = x10aux::alloc_printf("x10.util.GlobalRef<%s>(%p)", x10aux::getRTT<T>()->name(), data);
    return x10::lang::String::Steal(tmp);
}

template<class T> x10aux::ref<x10::lang::String> x10::util::GlobalRef<T>::typeName() {
    char* tmp = x10aux::alloc_printf("x10.util.GlobalRef<%s>", x10aux::getRTT<T>()->name());
    return x10::lang::String::Steal(tmp);
}

template<class T> x10aux::RuntimeType x10::util::GlobalRef<T>::rtt;

template<class T> x10aux::itable_entry x10::util::GlobalRef<T>::_itables[2] = {x10aux::itable_entry(x10aux::getRTT<x10::lang::Any>(), &GlobalRef_ithunk0<T>::itable),
                                                                               x10aux::itable_entry(NULL, (void*)x10aux::getRTT<x10::util::GlobalRef<T> >())};

template<class T> x10aux::itable_entry x10::util::GlobalRef<T>::_iboxitables[2] = {x10aux::itable_entry(x10aux::getRTT<x10::lang::Any>(), &GlobalRef_iboxithunk0<T>::itable),
                                                                                   x10aux::itable_entry(NULL, (void*)x10aux::getRTT<x10::util::GlobalRef<T> >())};

template<class T> void x10::util::GlobalRef<T>::_initRTT() {
    const x10aux::RuntimeType *canonical = x10aux::getRTT<x10::util::GlobalRef<void> >();
    if (rtt.initStageOne(canonical)) return;
    const x10aux::RuntimeType* parents[2] = { x10aux::getRTT<x10::lang::Any>(), x10aux::getRTT<x10::lang::Any>()};
    const x10aux::RuntimeType* params[1] = { x10aux::getRTT<T>()};
    x10aux::RuntimeType::Variance variances[1] = { x10aux::RuntimeType::invariant};
    const char *baseName = "x10.util.GlobalRef";
    rtt.initStageTwo(baseName, 2, parents, 1, params, variances);
}
#endif // X10_LANG_GLOBALREF_IMPLEMENTATION
#endif // __X10_LANG_GLOBALREF_NODEPS
