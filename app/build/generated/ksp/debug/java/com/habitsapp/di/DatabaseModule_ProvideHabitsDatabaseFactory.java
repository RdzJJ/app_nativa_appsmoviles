package com.habitsapp.di;

import android.content.Context;
import com.habitsapp.data.database.HabitsDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class DatabaseModule_ProvideHabitsDatabaseFactory implements Factory<HabitsDatabase> {
  private final Provider<Context> contextProvider;

  private DatabaseModule_ProvideHabitsDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public HabitsDatabase get() {
    return provideHabitsDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideHabitsDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideHabitsDatabaseFactory(contextProvider);
  }

  public static HabitsDatabase provideHabitsDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideHabitsDatabase(context));
  }
}
