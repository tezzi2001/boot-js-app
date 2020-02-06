package com.bondarenko.apps.boot_js_app.domain.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AdminPermissionLog {
}
