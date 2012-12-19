/**
 * Copyright 2011 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gwtplatform.mvp.rebind;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.gwtplatform.mvp.client.DelayedBindRegistry;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

import java.io.PrintWriter;

/**
 * Will generate a {@link com.gwtplatform.mvp.client.ApplicationController}. If the user wants his Generator to be
 * generated by GWTP, this Application controller will make sure that the Ginjector is used to trigger the initial
 * revealCurrentPlace() from the place manager.
 */
public class ApplicationControllerGenerator extends AbstractGenerator {
  private static final String SUFFIX = "Impl";
  private static final String OVERRIDE = "@Override";
  private static final String INJECT_METHOD = "public void init() {";
  private static final String DELAYED_BIND = "%s.bind(%s.SINGLETON);";
  private static final String PLACEMANAGER_REVEALCURRENTPLACE = "%s.SINGLETON.get%s().revealCurrentPlace();";

  @Override
  public String generate(TreeLogger treeLogger, GeneratorContext generatorContext, String typeName)
      throws UnableToCompleteException {
    setTypeOracle(generatorContext.getTypeOracle());
    setPropertyOracle(generatorContext.getPropertyOracle());
    setTreeLogger(treeLogger);
    setTypeClass(getType(typeName));

    PrintWriter printWriter;
    printWriter = tryCreatePrintWriter(generatorContext, SUFFIX);

    if (printWriter == null) {
      return typeName + SUFFIX;
    }

    ClassSourceFileComposerFactory composer = initComposer();
    SourceWriter sourceWriter = composer.createSourceWriter(generatorContext, printWriter);

    writeInit(sourceWriter,
        new GinjectorGenerator().generate(getTreeLogger(), generatorContext, GinjectorGenerator.DEFAULT_FQ_NAME));

    closeDefinition(sourceWriter);

    return getPackageName() + "." + getClassName();
  }

  private void writeInit(SourceWriter sourceWriter, String generatorName) {
    sourceWriter.println(OVERRIDE);
    sourceWriter.println(INJECT_METHOD);
    sourceWriter.indent();

    sourceWriter.println(String.format(DELAYED_BIND, DelayedBindRegistry.class.getSimpleName(), generatorName));
    sourceWriter.println();

    sourceWriter.println(String.format(PLACEMANAGER_REVEALCURRENTPLACE, generatorName,
        PlaceManager.class.getSimpleName()));
    sourceWriter.outdent();
    sourceWriter.println("}");
  }

  private ClassSourceFileComposerFactory initComposer() {
    ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(getPackageName(), getClassName());
    composer.addImport(getTypeClass().getQualifiedSourceName());
    composer.addImplementedInterface(getTypeClass().getName());

    composer.addImport(DelayedBindRegistry.class.getCanonicalName());

    return composer;
  }
}
