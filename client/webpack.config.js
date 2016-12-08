'use strict';

const webpack = require('webpack');
const isProd = process.env.NODE_ENV === 'production';


var config = {
  devtool: 'source-map',
  entry: './src/main/App.js',
  output: {
    path: './',
    filename: '../server/src/main/resources/static/js/bundle.js'
  },
  externals: {
    "jquery": 'jQuery',
    'page': 'page',
    'rivets': 'rivets',
    'lodash': 'lodash'
  },
  plugins: [
    new webpack.ProvidePlugin({
      $: 'jquery',
      jQuery: 'jquery',
      'window.jQuery': 'jquery',
      'page': 'page',
      'lodash': '_',
      'cookies': 'js-cookie'
    })
  ],
  module: {
    loaders: [
      {test: /\.(js)$/, loaders: ['babel'], exclude: /node_modules/},
      {test: /\.css$/, loader: 'style-loader!css-loader', exclude: /node_modules/}
    ]
  }
}

if (isProd) {
  config.plugins.push(
      new webpack.LoaderOptionsPlugin({
        minimize: true,
        debug: false
      }),
      new webpack.optimize.UglifyJsPlugin({
        compress: {
          warnings: false
        },
        output: {
          comments: false
        },
        sourceMap: false
      })
  );
}

module.exports = config;
