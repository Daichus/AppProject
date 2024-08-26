pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        jcenter()  //影片新增的
        //mavenCentral()  //影片把這個刪掉
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {  //影片新增的
            url = uri("https://jitpack.io")
        }
        google()
        //mavenCentral()  //影片把這個刪掉
        jcenter()  //影片新增的
    }
}

rootProject.name = "CodeLearning"
include(":app")
 