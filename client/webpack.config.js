'use strict';

const webpack = require('webpack');
const isProd = process.env.NODE_ENV === 'production';


var config = {
    devtool: 'cheap-module-eval-source-map',
    entry: './src/main/index.js',
    output: {
        path: './',
        filename: '../server/src/main/resources/static/js/bundle.js'
    },
    plugins: [],
    module: {
        loaders: [
            { test: /\.(js)$/, loaders: ['babel'], exclude: /node_modules/},
            { test: /\.css$/, loader: 'style-loader!css-loader', exclude: /node_modules/ }
        ]
    }
}

if (!isProd) {
    config.devtool = 'cheap-module-source-map';
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
