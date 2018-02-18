'use strict';
module.exports = (sequelize, DataTypes) => {
  var PersonUseCompany = sequelize.define('PersonUseCompany', {
    pointsBalance: { type: DataTypes.INTEGER, allowNull: false }
  }, {
    paranoid: true,
    freezeTableName: true
  });
  PersonUseCompany.associate = function(models) {
    // associations can be defined here
  };
  return PersonUseCompany;
};