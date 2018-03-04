'use strict';
module.exports = (sequelize, DataTypes) => {
    var Places = sequelize.define('Places', {
        streetNumber: { type: DataTypes.INTEGER, allowNull: true },
        route: { type: DataTypes.STRING, allowNull: true },
        city: { type: DataTypes.STRING, allowNull: false },
        zipCode: { type: DataTypes.STRING, allowNull: true },
        country: { type: DataTypes.STRING, allowNull: false },
    }, {
        paranoid: true,
        freezeTableName: true
    });
    Places.associate = function(models) {
        models.Places.hasOne(models.Companies, { foreignKey: { allowNull: false }})
        models.Places.hasOne(models.Persons, { foreignKey: { allowNull: false }})
    };
    return Places;
};