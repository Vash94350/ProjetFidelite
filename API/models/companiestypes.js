'use strict';
module.exports = (sequelize, DataTypes) => {
  var CompaniesTypes = sequelize.define('CompaniesTypes', {
    typeName: { type: DataTypes.STRING, allowNull: false, unique: true }
  }, {
    paranoid: true,
    freezeTableName: true
  });
  CompaniesTypes.associate = function(models) {
    models.CompaniesTypes.hasOne(models.Companies, { foreignKey: { allowNull: false }})
  };
  return CompaniesTypes;
};