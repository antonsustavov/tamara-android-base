package com.tamara.care.watch.data.base

abstract class BaseMapper<Entity, Model> {

    open fun mapEntityToModel(entity: Entity): Model =
        throw IllegalStateException("Unsupported operation!")

    open fun mapModelToEntity(model: Model): Entity =
        throw IllegalStateException("Unsupported operation!")

    open fun mapEntityToModel(entities: List<Entity>): List<Model> =
        entities.map(::mapEntityToModel)

    open fun mapModelToEntity(models: List<Model>): List<Entity> =
        models.map(::mapModelToEntity)

}