'use strict';
module.exports = (sequelize, DataTypes) => {
  var Persons = sequelize.define('Persons', {
    email: { type: DataTypes.STRING, allowNull: false, unique: true },
    password: { type: DataTypes.STRING, allowNull: false },
    telephone: { type: DataTypes.STRING, allowNull: false },
    firstname: { type: DataTypes.STRING, allowNull: false },
    lastname: { type: DataTypes.STRING, allowNull: false },
    sex: { type: DataTypes.CHAR(1), allowNull: false },
    birthDate: { type: DataTypes.DATEONLY, allowNull: false },
    isMailVerified: { type: DataTypes.BOOLEAN, allowNull: false, defaultValue: false },
    validationToken: { type: DataTypes.STRING, allowNull: false }
  }, {
    paranoid: true,
    freezeTableName: true
  });
  Persons.associate = function(models) {
    models.Persons.belongsToMany(models.Companies, {through: models.PersonUseCompany})
  };
  return Persons;
};