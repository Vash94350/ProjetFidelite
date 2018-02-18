'use strict';
module.exports = (sequelize, DataTypes) => {
  var Routes = sequelize.define('Routes', {
    nameRoute: { type: DataTypes.STRING, allowNull: false },
    constraintRoute: { type: DataTypes.STRING, allowNull: false },
    archive: { type: DataTypes.BOOLEAN, allowNull: false, defaultValue: false }
  }, {
    paranoid: true,
    freezeTableName: true
  });
  Routes.associate = function(models) {
    models.Routes.hasOne(models.PersonUseCompany, { foreignKey: { allowNull: false }})
  };
  return Routes;
};