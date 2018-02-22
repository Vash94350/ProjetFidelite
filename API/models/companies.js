'use strict';
module.exports = (sequelize, DataTypes) => {
  var Companies = sequelize.define('Companies', {
    email: { type: DataTypes.STRING, allowNull: false, unique: true },
    password: { type: DataTypes.STRING, allowNull: false },
    telephone: { type: DataTypes.STRING, allowNull: false },
    companyName: { type: DataTypes.STRING, allowNull: false },
    description: { type: DataTypes.STRING, allowNull: false },
    siret: { type: DataTypes.STRING, allowNull: false },
    creationDate: { type: DataTypes.DATE, allowNull: false },
    city: { type: DataTypes.STRING, allowNull: false },
    country: { type: DataTypes.STRING, allowNull: false },
    isMailVerified: { type: DataTypes.BOOLEAN, allowNull: false, defaultValue: false },
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