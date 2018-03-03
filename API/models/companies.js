'use strict';
module.exports = (sequelize, DataTypes) => {
  var Companies = sequelize.define('Companies', {
    email: { type: DataTypes.STRING, allowNull: false, unique: true },
    password: { type: DataTypes.STRING, allowNull: false },
    telephone: { type: DataTypes.STRING, allowNull: false },
    companyName: { type: DataTypes.STRING, allowNull: false },
    description: { type: DataTypes.STRING, allowNull: false },
    siret: { type: DataTypes.STRING, allowNull: false },
    creationDate: { type: DataTypes.DATEONLY, allowNull: false },
    isMailVerified: { type: DataTypes.BOOLEAN, allowNull: false, defaultValue: false },
    validationToken: { type: DataTypes.STRING, allowNull: false }
  }, {
    paranoid: true,
    freezeTableName: true
  });
  Companies.associate = function(models) {
    models.Companies.belongsToMany(models.Persons, {through: models.PersonUseCompany})
    models.Companies.hasOne(models.Routes)
  };
  return Companies;
};