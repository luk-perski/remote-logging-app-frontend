module.exports = {
    future: {
        removeDeprecatedGapUtilities: true,
        purgeLayersByDefault: true,
    },
    purge: [
        './src/**/*.tsx'
    ],
    theme: {
        extend: {
            width: {
                '76': '19rem',
                '88': '22rem',
                '184': '46rem',
                '208': '52rem',
                '312': '78rem',
                '336': '84rem',
                '1px': '1px',
                '2px': '2px',
                '3px': '3px',
            }
        },
    },
    variants: {},
    plugins: [],
}
