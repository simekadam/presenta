/*
 * Copyright 2013 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.presenta;

import android.app.Application;

import com.example.presenta.di.RootModule;
import com.example.presenta.model.Chats;

import dagger.Component;
import io.techery.presenta.addition.ActionBarOwner;
import io.techery.presenta.di.ApplicationScope;
import io.techery.presenta.mortar.DaggerService;
import mortar.MortarScope;

public class MortarDemoApplication extends Application {

  @ApplicationScope
  @Component(modules =  RootModule.class)
  public interface AppComponent {
    void inject(MortarDemoApplication application);
    ActionBarOwner actionBarOwner();
    Chats chats();
  }

  private MortarScope rootScope;

  @Override public void onCreate() {
    super.onCreate();
    instance = this;

    AppComponent component = DaggerService.createComponent(AppComponent.class);
    rootScope = MortarScope.buildRootScope()
        .withService(DaggerService.SERVICE_NAME, component)
        .build("root");
    component.inject(this);
  }

  public static MortarDemoApplication instance() {
    return instance;
  }

  private static MortarDemoApplication instance;

  @Override public Object getSystemService(String name) {
    try {
      return rootScope.getService(name);

    } catch (IllegalArgumentException ex) {
      return super.getSystemService(name);
    }
  }
}
