package com.jerry.companies.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jerry.companies.R
import com.jerry.companies.cache.data.Company
import com.jerry.companies.cache.data.Revenue
import com.jerry.companies.extensions.prettyCount
import com.jerry.companies.ui.common.LocalAppBarTitle
import com.jerry.companies.ui.common.unboundClickable
import com.jerry.companies.util.READABLE_DATE_PATTERN_PARSER
import com.patrykandpatryk.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.startAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.line.lineChart
import com.patrykandpatryk.vico.core.axis.AxisPosition
import com.patrykandpatryk.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatryk.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.entry.FloatEntry
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel


@Destination
@Composable
fun DetailsMain(
    companyId: Long,
    viewModel: DetailsViewModel = koinViewModel()
) {
    val detailsFlow = viewModel.companyFlow.collectAsStateWithLifecycle(null)

    LocalAppBarTitle.current(detailsFlow.value?.company?.name.orEmpty())

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        detailsFlow.value?.let { companyAndRevenue ->
            CompanyAddress(companyAndRevenue.company)
            CompanyRevenue(companyAndRevenue.revenue)
        }
    }
}

@Composable
private fun CompanyAddress(company: Company) {
    Text(
        style = MaterialTheme.typography.titleLarge,
        text = stringResource(R.string.address)
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1F)) {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                text = company.address
            )
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = company.city
            )
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = company.country
            )
        }
        val context = LocalContext.current
        Image(
            modifier = Modifier
                .unboundClickable {
                    context.startActivity(company.mapIntent)
                }
                .padding(horizontal = 16.dp),
            painter = painterResource(R.drawable.ic_google_maps),
            contentDescription = stringResource(R.string.view_on_map)
        )
    }
}

@Composable
private fun CompanyRevenue(revenue: List<Revenue>) {
    Text(
        modifier = Modifier.padding(top = 16.dp),
        style = MaterialTheme.typography.titleLarge,
        text = stringResource(R.string.revenue)
    )

    val producer = ChartEntryModelProducer(revenue.sortedBy { it.seq }
        .mapIndexed { index, item ->
            FloatEntry(index.toFloat(), item.value.toFloat())
        })
    val xFormatter = rememberColumnChartXAxisValueFormatter(revenue)
    val yFormatter = rememberColumnChartYAxisValueFormatter()

    Chart(
        modifier = Modifier.padding(top = 16.dp),
        chart = lineChart(),
        chartModelProducer = producer,
        startAxis = startAxis(
            horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
            valueFormatter = yFormatter
        ),
        bottomAxis = bottomAxis(
            valueFormatter = xFormatter
        ),
    )
}

@Composable
private fun rememberColumnChartXAxisValueFormatter(revenue: List<Revenue>)
        : AxisValueFormatter<AxisPosition.Horizontal.Bottom> =
    AxisValueFormatter { x, _ ->
        val value = revenue.getOrNull(x.toInt())
        READABLE_DATE_PATTERN_PARSER.format(value?.date)
    }

@Composable
private fun rememberColumnChartYAxisValueFormatter()
        : AxisValueFormatter<AxisPosition.Vertical.Start> =
    AxisValueFormatter { x, _ ->
        x.prettyCount.orEmpty()
    }